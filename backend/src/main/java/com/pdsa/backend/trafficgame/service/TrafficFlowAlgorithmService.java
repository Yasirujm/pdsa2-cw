package com.pdsa.backend.trafficgame.service;

import com.pdsa.backend.trafficgame.DTOs.TrafficEdgeResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class TrafficFlowAlgorithmService {

    private static final String[] NODES = {"A", "B", "C", "D", "E", "F", "G", "H", "T"};

    private static final String[] EDGE_FROM = {"A", "A", "A", "B", "B", "C", "C", "D", "E", "E", "F", "G", "H"};
    private static final String[] EDGE_TO = {"B", "C", "D", "E", "F", "E", "F", "F", "G", "H", "H", "T", "T"};

    private final Random random = new Random();

    public List<String> getNodes() {
        return Arrays.asList(NODES);
    }

    public String getSourceNode() {
        return "A";
    }

    public String getSinkNode() {
        return "T";
    }

    public List<TrafficEdgeResponse> generateRandomEdges(int minCapacity, int maxCapacity) {
        List<TrafficEdgeResponse> edges = new ArrayList<>();
        for (int i = 0; i < EDGE_FROM.length; i++) {
            int capacity = minCapacity + random.nextInt(maxCapacity - minCapacity + 1);
            edges.add(new TrafficEdgeResponse(EDGE_FROM[i], EDGE_TO[i], capacity, 0));
        }
        return edges;
    }

    public MaxFlowResult solveWithEdmondsKarp(List<TrafficEdgeResponse> edges, String source, String sink) {
        int[][] capacity = buildCapacityMatrix(edges);
        int[][] residual = copyMatrix(capacity);

        int sourceIndex = nodeIndex(source);
        int sinkIndex = nodeIndex(sink);
        int[] parent = new int[NODES.length];

        int maxFlow = 0;
        long start = System.nanoTime();

        while (bfs(residual, sourceIndex, sinkIndex, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int node = sinkIndex; node != sourceIndex; node = parent[node]) {
                int prev = parent[node];
                pathFlow = Math.min(pathFlow, residual[prev][node]);
            }

            for (int node = sinkIndex; node != sourceIndex; node = parent[node]) {
                int prev = parent[node];
                residual[prev][node] -= pathFlow;
                residual[node][prev] += pathFlow;
            }
            maxFlow += pathFlow;
        }

        long elapsed = System.nanoTime() - start;
        return new MaxFlowResult(maxFlow, elapsed, buildEdgeFlows(capacity, residual));
    }

    public MaxFlowResult solveWithFordFulkerson(List<TrafficEdgeResponse> edges, String source, String sink) {
        int[][] capacity = buildCapacityMatrix(edges);
        int[][] residual = copyMatrix(capacity);
        int sourceIndex = nodeIndex(source);
        int sinkIndex = nodeIndex(sink);

        int maxFlow = 0;
        long start = System.nanoTime();

        int pathFlow;
        while ((pathFlow = dfsAugment(sourceIndex, sinkIndex, residual, new boolean[NODES.length], Integer.MAX_VALUE)) > 0) {
            maxFlow += pathFlow;
        }

        long elapsed = System.nanoTime() - start;
        return new MaxFlowResult(maxFlow, elapsed, buildEdgeFlows(capacity, residual));
    }

    private int dfsAugment(int node, int sink, int[][] residual, boolean[] visited, int flow) {
        if (node == sink) {
            return flow;
        }

        visited[node] = true;
        for (int next = 0; next < NODES.length; next++) {
            if (visited[next] || residual[node][next] <= 0) {
                continue;
            }
            int pushed = dfsAugment(next, sink, residual, visited, Math.min(flow, residual[node][next]));
            if (pushed > 0) {
                residual[node][next] -= pushed;
                residual[next][node] += pushed;
                return pushed;
            }
        }
        return 0;
    }

    private boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        Arrays.fill(parent, -1);
        parent[source] = source;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.offer(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            for (int next = 0; next < NODES.length; next++) {
                if (parent[next] == -1 && residual[current][next] > 0) {
                    parent[next] = current;
                    if (next == sink) {
                        return true;
                    }
                    queue.offer(next);
                }
            }
        }
        return false;
    }

    private int[][] buildCapacityMatrix(List<TrafficEdgeResponse> edges) {
        int[][] capacity = new int[NODES.length][NODES.length];
        for (TrafficEdgeResponse edge : edges) {
            int fromIndex = nodeIndex(edge.getFrom());
            int toIndex = nodeIndex(edge.getTo());
            capacity[fromIndex][toIndex] = edge.getCapacity();
        }
        return capacity;
    }

    private int[][] copyMatrix(int[][] input) {
        int[][] copy = new int[input.length][input.length];
        for (int i = 0; i < input.length; i++) {
            System.arraycopy(input[i], 0, copy[i], 0, input[i].length);
        }
        return copy;
    }

    private Map<String, Integer> buildEdgeFlows(int[][] capacity, int[][] residual) {
        Map<String, Integer> flows = new LinkedHashMap<>();
        for (int i = 0; i < EDGE_FROM.length; i++) {
            int fromIndex = nodeIndex(EDGE_FROM[i]);
            int toIndex = nodeIndex(EDGE_TO[i]);
            int usedFlow = capacity[fromIndex][toIndex] - residual[fromIndex][toIndex];
            flows.put(EDGE_FROM[i] + "-" + EDGE_TO[i], usedFlow);
        }
        return flows;
    }

    private int nodeIndex(String node) {
        for (int i = 0; i < NODES.length; i++) {
            if (NODES[i].equals(node)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown node: " + node);
    }

    public static class MaxFlowResult {
        private final int maxFlow;
        private final long timeNanos;
        private final Map<String, Integer> edgeFlows;

        public MaxFlowResult(int maxFlow, long timeNanos, Map<String, Integer> edgeFlows) {
            this.maxFlow = maxFlow;
            this.timeNanos = timeNanos;
            this.edgeFlows = edgeFlows;
        }

        public int getMaxFlow() {
            return maxFlow;
        }

        public long getTimeNanos() {
            return timeNanos;
        }

        public Map<String, Integer> getEdgeFlows() {
            return edgeFlows;
        }
    }
}
