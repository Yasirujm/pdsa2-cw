package com.pdsa.backend.knightstour.service;

import com.pdsa.backend.knightstour.DTOs.KnightTourResult;
import org.springframework.stereotype.Service;

@Service
public class KnightTourService {

    private static final int[] ROW_MOVES = {-2, -1, 1, 2, 2, 1, -1, -2};
    private static final int[] COL_MOVES = {1, 2, 2, 1, -1, -2, -2, -1};

    public int[][] createEmptyBoard(int size) {
        validateBoardSize(size);

        int[][] board = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                board[row][col] = -1;
            }
        }

        return board;
    }

    public boolean isValidMove(int[][] board, int row, int col) {
        int size = board.length;

        return row >= 0 &&
                col >= 0 &&
                row < size &&
                col < size &&
                board[row][col] == -1;
    }

    public int[] getRowMoves() {
        return ROW_MOVES.clone();
    }

    public int[] getColMoves() {
        return COL_MOVES.clone();
    }

    public void validateBoardSize(int size) {
        if (size != 8 && size != 16) {
            throw new IllegalArgumentException("Board size must be either 8 or 16");
        }
    }

    private boolean dfs(int[][] board, int row, int col, int step) {
        int size = board.length;

        // base case: all cells visited
        if (step == size * size) {
            return true;
        }

        for (int i = 0; i < 8; i++) {
            int newRow = row + ROW_MOVES[i];
            int newCol = col + COL_MOVES[i];

            if (isValidMove(board, newRow, newCol)) {
                board[newRow][newCol] = step;

                if (dfs(board, newRow, newCol, step + 1)) {
                    return true;
                }

                // backtrack
                board[newRow][newCol] = -1;
            }
        }

        return false;
    }

    public int[][] solveWithDFS(int size, int startRow, int startCol) {
        int[][] board = createEmptyBoard(size);

        // first move
        board[startRow][startCol] = 0;

        if (dfs(board, startRow, startCol, 1)) {
            return board;
        } else {
            throw new RuntimeException("No solution found");
        }
    }

    public int[] generateRandomStart(int size) {
        validateBoardSize(size);

        int row = (int) (Math.random() * size);
        int col = (int) (Math.random() * size);

        return new int[]{row, col};
    }

    public KnightTourResult solveDFSWithTime(int size) {
        int[] start = generateRandomStart(size);
        int startRow = start[0];
        int startCol = start[1];

        long startTime = System.currentTimeMillis();

        int[][] board = solveWithDFS(size, startRow, startCol);

        long endTime = System.currentTimeMillis();

        long timeTaken = endTime - startTime;

        return new KnightTourResult(board, startRow, startCol, timeTaken);
    }

    // Greedy algo

    public int[][] solveWithGreedy(int size, int startRow, int startCol) {
        int[][] board = createEmptyBoard(size);

        board[startRow][startCol] = 0;

        int currentRow = startRow;
        int currentCol = startCol;

        for (int step = 1; step < size * size; step++) {

            int minDegree = Integer.MAX_VALUE;
            int nextRow = -1;
            int nextCol = -1;

            for (int i = 0; i < 8; i++) {
                int newRow = currentRow + ROW_MOVES[i];
                int newCol = currentCol + COL_MOVES[i];

                if (isValidMove(board, newRow, newCol)) {
                    int degree = countOnwardMoves(board, newRow, newCol);

                    if (degree < minDegree) {
                        minDegree = degree;
                        nextRow = newRow;
                        nextCol = newCol;
                    }
                }
            }

            if (nextRow == -1) {
                throw new RuntimeException("Greedy failed to find solution");
            }

            board[nextRow][nextCol] = step;
            currentRow = nextRow;
            currentCol = nextCol;
        }

        return board;
    }

    private int countOnwardMoves(int[][] board, int row, int col) {
        int count = 0;

        for (int i = 0; i < 8; i++) {
            int newRow = row + ROW_MOVES[i];
            int newCol = col + COL_MOVES[i];

            if (isValidMove(board, newRow, newCol)) {
                count++;
            }
        }

        return count;
    }

    public KnightTourResult solveGreedyWithTime(int size) {

        int maxAttempts = 50; // try multiple times

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            int[] start = generateRandomStart(size);
            int startRow = start[0];
            int startCol = start[1];

            long startTime = System.currentTimeMillis();

            try {
                int[][] board = solveWithGreedy(size, startRow, startCol);

                long endTime = System.currentTimeMillis();

                long timeTaken = endTime - startTime;

                return new KnightTourResult(board, startRow, startCol, timeTaken);

            } catch (RuntimeException e) {
                // try again with different start
            }
        }

        throw new RuntimeException("Greedy failed after multiple attempts");
    }
}