package com.pdsa.queens.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.*;

/**
 * Threaded backtracking solver for 16-Queens.
 * Strategy: 16 threads run in parallel, each fixing the first queen in one column (0–15).
 * This divides the search space evenly across threads.
 *
 * Algorithm complexity: O(n!/16) per thread in parallel — 16x faster in theory.
 * Space complexity: O(n) per thread for recursion stack.
 */
@Component
public class ThreadedQueensSolver {

    private static final int N = 16;

    public List<int[]> findAllSolutions() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(N);
        List<Future<List<int[]>>> futures = new ArrayList<>();

        // Each thread handles one starting row for the first queen
        for (int startRow = 0; startRow < N; startRow++) {
            final int row = startRow;
            futures.add(executor.submit(() -> solveFromRow(row)));
        }

        List<int[]> allSolutions = new ArrayList<>();
        for (Future<List<int[]>> future : futures) {
            allSolutions.addAll(future.get());
        }

        executor.shutdown();
        return allSolutions;
    }

    private List<int[]> solveFromRow(int firstQueenRow) {
        List<int[]> solutions = new ArrayList<>();
        int[] queens = new int[N];
        queens[0] = firstQueenRow;   // Fix first queen position
        solve(queens, 1, solutions);
        return solutions;
    }

    private void solve(int[] queens, int col, List<int[]> solutions) {
        if (col == N) {
            solutions.add(queens.clone());
            return;
        }
        for (int row = 0; row < N; row++) {
            if (isSafe(queens, col, row)) {
                queens[col] = row;
                solve(queens, col + 1, solutions);
            }
        }
    }

    private boolean isSafe(int[] queens, int col, int row) {
        for (int c = 0; c < col; c++) {
            if (queens[c] == row) return false;
            if (Math.abs(queens[c] - row) == Math.abs(c - col)) return false;
        }
        return true;
    }
}