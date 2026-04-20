package com.pdsa.backend.queens.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SequentialQueensSolver {

    private static final int BOARD = 16;
    private static final int QUEENS = 8;
    public static final int MAX_SOLUTIONS = 1_000_000;
    // Practical limit — full solution space is too large to store

    private final List<int[]> solutions = new ArrayList<>();
    private boolean limitReached = false;

    public List<int[]> findAllSolutions() {
        solutions.clear();
        limitReached = false;
        boolean[] usedRows = new boolean[BOARD];
        boolean[] usedCols = new boolean[BOARD];
        int[] queens = new int[QUEENS];
        solve(queens, usedRows, usedCols, 0);
        return new ArrayList<>(solutions);
    }

    private void solve(int[] queens, boolean[] usedRows,
                       boolean[] usedCols, int index) {
        if (limitReached) return;
        // Stop recursing once limit is reached

        if (index == QUEENS) {
            int[] sorted = queens.clone();
            Arrays.sort(sorted);
            solutions.add(sorted);
            if (solutions.size() >= MAX_SOLUTIONS) {
                limitReached = true;
            }
            return;
        }

        for (int col = 0; col < BOARD; col++) {
            if (limitReached) return;
            if (usedCols[col]) continue;
            for (int row = 0; row < BOARD; row++) {
                if (limitReached) return;
                if (usedRows[row]) continue;
                if (isSafe(queens, index, col, row)) {
                    queens[index] = col * BOARD + row;
                    usedRows[row] = true;
                    usedCols[col] = true;
                    solve(queens, usedRows, usedCols, index + 1);
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