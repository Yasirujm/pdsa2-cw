package com.pdsa.backend.snakesladder.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SnakeLadderSolverService {
    public int minThrowsBfs(int boardSize, Map<Integer, Integer> jumps) {
        int totalCells = boardSize * boardSize;
        boolean[] visited = new boolean[totalCells + 1];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{1, 0});
        visited[1] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cell = current[0];
            int throwsUsed = current[1];
            if (cell == totalCells) {
                return throwsUsed;
            }
            for (int dice = 1; dice <= 6; dice++) {
                int next = cell + dice;
                if (next > totalCells) continue;
                next = jumps.getOrDefault(next, next);
                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(new int[]{next, throwsUsed + 1});
                }
            }
        }
        return -1;
    }

    public int minThrowsDijkstra(int boardSize, Map<Integer, Integer> jumps) {
        int totalCells = boardSize * boardSize;
        int[] dist = new int[totalCells + 1];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[1] = 0;
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(State::throwsUsed));
        pq.offer(new State(1, 0));

        while (!pq.isEmpty()) {
            State current = pq.poll();
            if (current.throwsUsed > dist[current.cell]) continue;
            if (current.cell == totalCells) return current.throwsUsed;
            for (int dice = 1; dice <= 6; dice++) {
                int next = current.cell + dice;
                if (next > totalCells) continue;
                next = jumps.getOrDefault(next, next);
                int nextCost = current.throwsUsed + 1;
                if (nextCost < dist[next]) {
                    dist[next] = nextCost;
                    pq.offer(new State(next, nextCost));
                }
            }
        }
        return -1;
    }

    private static class State {
        private final int cell;
        private final int throwsUsed;

        public State(int cell, int throwsUsed) {
            this.cell = cell;
            this.throwsUsed = throwsUsed;
        }

        public int throwsUsed() {
            return throwsUsed;
        }
    }
}