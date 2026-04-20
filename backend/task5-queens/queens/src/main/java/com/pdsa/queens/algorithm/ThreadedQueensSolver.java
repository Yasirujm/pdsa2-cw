package com.pdsa.queens.algorithm;

import org.springframework.stereotype.Component;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ThreadedQueensSolver {

    private static final int BOARD = 16;
    private static final int QUEENS = 8;
    private static final int MAX_SOLUTIONS = 1_000_000;

    public List<int[]> findAllSolutions()
            throws InterruptedException, ExecutionException {

        ExecutorService executor = Executors.newFixedThreadPool(BOARD);
        List<Future<List<int[]>>> futures = new ArrayList<>();

        // Shared counter across all threads
        AtomicInteger totalFound = new AtomicInteger(0);

        for (int startCol = 0; startCol < BOARD; startCol++) {
            final int col = startCol;
            futures.add(executor.submit(
                    () -> solveFromCol(col, totalFound)
            ));
        }

        List<int[]> allSolutions = new ArrayList<>();
        for (Future<List<int[]>> future : futures) {
            List<int[]> partial = future.get();
            allSolutions.addAll(partial);
            // Stop collecting once we have enough
            if (allSolutions.size() >= MAX_SOLUTIONS) break;
        }

        executor.shutdown();

        // Trim to exactly 1 million if slightly over
        if (allSolutions.size() > MAX_SOLUTIONS) {
            allSolutions = allSolutions.subList(0, MAX_SOLUTIONS);
        }

        return allSolutions;
    }

    private List<int[]> solveFromCol(int firstCol,
                                     AtomicInteger totalFound) {
        List<int[]> solutions = new ArrayList<>();

        for (int firstRow = 0; firstRow < BOARD; firstRow++) {
            if (totalFound.get() >= MAX_SOLUTIONS) break;

            int[] queens = new int[QUEENS];
            boolean[] usedRows = new boolean[BOARD];
            boolean[] usedCols = new boolean[BOARD];
            queens[0] = firstCol * BOARD + firstRow;
            usedRows[firstRow] = true;
            usedCols[firstCol] = true;
            solve(queens, usedRows, usedCols, 1,
                    solutions, totalFound);
        }
        return solutions;
    }

    private void solve(int[] queens, boolean[] usedRows,
                       boolean[] usedCols, int index,
                       List<int[]> solutions,
                       AtomicInteger totalFound) {

        if (totalFound.get() >= MAX_SOLUTIONS) return;

        if (index == QUEENS) {
            int[] sorted = queens.clone();
            Arrays.sort(sorted);
            solutions.add(sorted);
            totalFound.incrementAndGet();
            return;
        }

        for (int col = 0; col < BOARD; col++) {
            if (totalFound.get() >= MAX_SOLUTIONS) return;
            if (usedCols[col]) continue;
            for (int row = 0; row < BOARD; row++) {
                if (totalFound.get() >= MAX_SOLUTIONS) return;
                if (usedRows[row]) continue;
                if (isSafe(queens, index, col, row)) {
                    queens[index] = col * BOARD + row;
                    usedRows[row] = true;
                    usedCols[col] = true;
                    solve(queens, usedRows, usedCols, index + 1,
                            solutions, totalFound);
                    usedRows[row] = false;
                    usedCols[col] = false;
                }
            }
        }
    }

    private boolean isSafe(int[] queens, int index, int col, int row) {
        for (int i = 0; i < index; i++) {
            int prevCol = queens[i] / BOARD;
            int prevRow = queens[i] % BOARD;
            if (Math.abs(prevRow - row) == Math.abs(prevCol - col))
                return false;
        }
        return true;
    }
}