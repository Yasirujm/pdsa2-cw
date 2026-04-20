package com.pdsa.queens;

public class QueenCountTest {

    private static final int BOARD = 16;
    private static final int QUEENS = 8;
    private static long count = 0;

    public static void main(String[] args) {
        System.out.println("Counting 8 queens on 16x16 board...");
        System.out.println("Please wait...");

        long startTime = System.currentTimeMillis();

        boolean[] usedRows = new boolean[BOARD];
        boolean[] usedCols = new boolean[BOARD];
        int[] queens = new int[QUEENS];

        solve(queens, usedRows, usedCols, 0);

        long endTime = System.currentTimeMillis();
        long seconds = (endTime - startTime) / 1000;

        System.out.println("================================");
        System.out.println("Total solutions found: " + count);
        System.out.println("Time taken: " + (endTime - startTime) + "ms (" + seconds + " seconds)");
        System.out.println("================================");

        if (count < 100_000) {
            System.out.println("VERDICT: Safe to store all solutions in DB");
        } else if (count < 1_000_000) {
            System.out.println("VERDICT: Manageable but will take time");
        } else if (count < 50_000_000) {
            System.out.println("VERDICT: Very large - storing all is risky");
        } else {
            System.out.println("VERDICT: Too large - do NOT store all solutions");
        }
    }

    private static void solve(int[] queens, boolean[] usedRows,
                              boolean[] usedCols, int index) {
        if (index == QUEENS) {
            count++;
            // Just count - do not store anything
            if (count % 1_000_000 == 0) {
                System.out.println("Found " + count + " solutions so far...");
            }
            return;
        }
        for (int col = 0; col < BOARD; col++) {
            if (usedCols[col]) continue;
            for (int row = 0; row < BOARD; row++) {
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

    private static boolean isSafe(int[] queens, int index, int col, int row) {
        for (int i = 0; i < index; i++) {
            int prevCol = queens[i] / BOARD;
            int prevRow = queens[i] % BOARD;
            if (Math.abs(prevRow - row) == Math.abs(prevCol - col))
                return false;
        }
        return true;
    }
}