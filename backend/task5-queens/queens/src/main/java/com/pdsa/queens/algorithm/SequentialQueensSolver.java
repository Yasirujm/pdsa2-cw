package com.pdsa.queens.algorithm;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Sequential (single-threaded) backtracking solver for 16-Queens.
 * Algorithm complexity: O(n!) in worst case, but pruning makes it feasible.
 * Space complexity: O(n) for recursion stack.
 */
@Component
public class SequentialQueensSolver {

    private static final int N = 16;
    private final List<int[]> solutions = new ArrayList<>();

    /**
     * Find all valid 16-queens solutions.
     * @return list of solutions (each solution is int[16] where index=column, value=row)
     */
    public List<int[]> findAllSolutions() {
        solutions.clear();
        int[] queens = new int[N]; // queens[col] = row of queen in that column
        solve(queens, 0);
        return new ArrayList<>(solutions);
    }

    private void solve(int[] queens, int col) {
        if (col == N) {
            solutions.add(queens.clone());
            return;
        }
        for (int row = 0; row < N; row++) {
            if (isSafe(queens, col, row)) {
                queens[col] = row;
                solve(queens, col + 1);
            }
        }
    }

    /**
     * Check if placing a queen at (col, row) is safe.
     * A placement is safe if no previously placed queen shares the same row or diagonal.
     */
    private boolean isSafe(int[] queens, int col, int row) {
        for (int c = 0; c < col; c++) {
            if (queens[c] == row) return false;                     // same row
            if (Math.abs(queens[c] - row) == Math.abs(c - col)) return false; // diagonal
        }
        return true;
    }
}