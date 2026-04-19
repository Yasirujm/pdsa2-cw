package com.pdsa.backend.minimumcost.service;

import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class MinCostMaxFlowSolverService {
    private static class Edge {
        int to;
        int rev;
        int capacity;
        int cost;

        Edge(int to, int rev, int capacity, int cost) {
            this.to = to;
            this.rev = rev;
            this.capacity = capacity;
            this.cost = cost;
        }
    }

    public int solve(int[][] matrix) {
        int n = matrix.length;
        int totalNodes = 2 * n + 2;
        int source = 0;
        int sink = totalNodes - 1;
        List<List<Edge>> graph = new ArrayList<>();

        for (int i = 0; i < totalNodes; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < n; i++) {
            addEdge(graph, source, 1 + i, 1, 0);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                addEdge(graph, 1 + i, 1 + n + j, 1, matrix[i][j]);
            }
        }

        for (int j = 0; j < n; j++) {
            addEdge(graph, 1 + n + j, sink, 1, 0);
        }

        int flow = 0;
        int cost = 0;
        while (flow < n) {
            int[] dist = new int[totalNodes];
            int[] parentNode = new int[totalNodes];
            int[] parentEdge = new int[totalNodes];
            boolean[] inQueue = new boolean[totalNodes];
            Arrays.fill(dist, Integer.MAX_VALUE);
            dist[source] = 0;
            Queue<Integer> q = new LinkedList<>();
            q.offer(source);
            inQueue[source] = true;
            while (!q.isEmpty()) {
                int u = q.poll();
                inQueue[u] = false;
                for (int i = 0; i < graph.get(u).size(); i++) {
                    Edge e = graph.get(u).get(i);
                    if (e.capacity > 0 && dist[u] != Integer.MAX_VALUE &&
                            dist[u] + e.cost < dist[e.to]) {
                        dist[e.to] = dist[u] + e.cost;
                        parentNode[e.to] = u;
                        parentEdge[e.to] = i;
                        if (!inQueue[e.to]) {
                            q.offer(e.to);
                            inQueue[e.to] = true;
                        }
                    }
                }
            }
            if (dist[sink] == Integer.MAX_VALUE) {
                throw new IllegalStateException("Unable to find full assignment");
            }

            int augFlow = 1;
            flow += augFlow;
            cost += dist[sink] * augFlow;
            int v = sink;
            while (v != source) {
                int u = parentNode[v];
                Edge e = graph.get(u).get(parentEdge[v]);
                e.capacity -= augFlow;
                graph.get(v).get(e.rev).capacity += augFlow;
                v = u;
            }
        }
        return cost;
    }

    private void addEdge(List<List<Edge>> graph, int from, int to, int capacity, int cost) {
        Edge forward = new Edge(to, graph.get(to).size(), capacity, cost);
        Edge backward = new Edge(from, graph.get(from).size(), 0, -cost);
        graph.get(from).add(forward);
        graph.get(to).add(backward);
    }
}

