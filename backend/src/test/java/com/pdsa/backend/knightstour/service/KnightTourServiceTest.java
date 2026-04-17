package com.pdsa.backend.knightstour.service;

import com.pdsa.backend.knightstour.DTOs.KnightTourResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnightTourServiceTest {

    private final KnightTourService knightTourService = new KnightTourService();

    @Test
    void shouldCreateEmptyBoardForSize8() {
        int[][] board = knightTourService.createEmptyBoard(8);

        assertEquals(8, board.length);
        assertEquals(8, board[0].length);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(-1, board[row][col]);
            }
        }
    }

    @Test
    void shouldCreateEmptyBoardForSize16() {
        int[][] board = knightTourService.createEmptyBoard(16);

        assertEquals(16, board.length);
        assertEquals(16, board[0].length);

        for (int row = 0; row < 16; row++) {
            for (int col = 0; col < 16; col++) {
                assertEquals(-1, board[row][col]);
            }
        }
    }

    @Test
    void shouldThrowExceptionForInvalidBoardSize() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> knightTourService.createEmptyBoard(10)
        );

        assertEquals("Board size must be either 8 or 16", exception.getMessage());
    }

    @Test
    void shouldReturnTrueForValidUnvisitedCell() {
        int[][] board = knightTourService.createEmptyBoard(8);

        boolean result = knightTourService.isValidMove(board, 3, 4);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseForVisitedCell() {
        int[][] board = knightTourService.createEmptyBoard(8);
        board[3][4] = 0;

        boolean result = knightTourService.isValidMove(board, 3, 4);

        assertFalse(result);
    }

    @Test
    void shouldReturnFalseForOutOfBoundsRow() {
        int[][] board = knightTourService.createEmptyBoard(8);

        assertFalse(knightTourService.isValidMove(board, -1, 4));
        assertFalse(knightTourService.isValidMove(board, 8, 4));
    }

    @Test
    void shouldReturnFalseForOutOfBoundsColumn() {
        int[][] board = knightTourService.createEmptyBoard(8);

        assertFalse(knightTourService.isValidMove(board, 4, -1));
        assertFalse(knightTourService.isValidMove(board, 4, 8));
    }

    @Test
    void shouldProvideEightKnightMoves() {
        int[] rowMoves = knightTourService.getRowMoves();
        int[] colMoves = knightTourService.getColMoves();

        assertEquals(8, rowMoves.length);
        assertEquals(8, colMoves.length);
    }

    @Test
    void shouldSolveKnightTourFor8x8() {
        int size = 8;
        int startRow = 0;
        int startCol = 0;

        int[][] board = knightTourService.solveWithDFS(size, startRow, startCol);

        assertNotNull(board);

        // check all cells visited
        boolean[] visited = new boolean[size * size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board[row][col];

                assertTrue(value >= 0 && value < size * size);
                visited[value] = true;
            }
        }

        // ensure all steps exist
        for (boolean v : visited) {
            assertTrue(v);
        }
    }

    @Test
    void shouldGenerateRandomStartAndSolve() {
        KnightTourResult result = knightTourService.solveDFSWithTime(8);

        assertNotNull(result);
        assertNotNull(result.getBoard());

        int size = result.getBoard().length;

        assertTrue(result.getStartRow() >= 0 && result.getStartRow() < size);
        assertTrue(result.getStartCol() >= 0 && result.getStartCol() < size);

        assertTrue(result.getTimeTaken() >= 0);
    }

    @Test
    void shouldSolveKnightTourUsingGreedy() {
        int size = 8;

        int[][] board = knightTourService.solveWithGreedy(size, 0, 0);

        assertNotNull(board);

        boolean[] visited = new boolean[size * size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board[row][col];

                assertTrue(value >= 0 && value < size * size);
                visited[value] = true;
            }
        }

        for (boolean v : visited) {
            assertTrue(v);
        }
    }
}