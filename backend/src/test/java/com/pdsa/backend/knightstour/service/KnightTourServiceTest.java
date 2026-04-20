package com.pdsa.backend.knightstour.service;

import com.pdsa.backend.knightstour.DTOs.KnightTourResult;
import org.junit.jupiter.api.Test;
import com.pdsa.backend.knightstour.repositories.KnightTourGameResultRepository;
import org.mockito.Mockito;
import com.pdsa.backend.knightstour.DTOs.KnightTourSolveResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourValidateResponse;

import static org.junit.jupiter.api.Assertions.*;

class KnightTourServiceTest {

    private final KnightTourGameResultRepository mockRepository =
            Mockito.mock(KnightTourGameResultRepository.class);

    private final KnightTourService knightTourService =
            new KnightTourService(mockRepository);
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

  /*  @Test
    void shouldGenerateRandomStartAndSolve() {
        KnightTourResult result = knightTourService.solveDFSWithTime(8);

        assertNotNull(result);
        assertNotNull(result.getBoard());

        int size = result.getBoard().length;

        assertTrue(result.getStartRow() >= 0 && result.getStartRow() < size);
        assertTrue(result.getStartCol() >= 0 && result.getStartCol() < size);

        assertTrue(result.getTimeTaken() >= 0);
    }*/

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

   /* @Test
    void shouldSolveGameAndReturnBoards() {
        KnightTourSolveResponse response = knightTourService.solveGame(8, 0, 0);

        assertNotNull(response);
        assertEquals(8, response.getBoardSize());
        assertEquals(0, response.getStartRow());
        assertEquals(0, response.getStartCol());

        assertNotNull(response.getDfsBoard());
        assertNotNull(response.getGreedyBoard());

        assertTrue(response.getDfsTimeTaken() >= 0);
        assertTrue(response.getGreedyTimeTaken() >= 0);
    }*/

    @Test
    void shouldValidateCorrectKnightTourBoard() {
        int[][] board = {
                {51, 0, 55, 36, 53, 2, 19, 10},
                {56, 37, 52, 1, 20, 11, 22, 3},
                {33, 50, 35, 54, 23, 18, 9, 12},
                {38, 57, 32, 45, 40, 21, 4, 17},
                {49, 34, 39, 24, 61, 8, 13, 26},
                {58, 31, 44, 41, 46, 25, 16, 5},
                {43, 48, 29, 60, 7, 62, 27, 14},
                {30, 59, 42, 47, 28, 15, 6, 63}
        };

        KnightTourValidateResponse response =
                knightTourService.validateSubmittedBoard(8, 0, 1, board);

        assertTrue(response.isCorrect());
        assertEquals("Valid Knight's Tour", response.getMessage());
    }

    @Test
    void shouldRejectBoardWithWrongStartPosition() {
        int[][] board = {
                {51, 0, 55, 36, 53, 2, 19, 10},
                {56, 37, 52, 1, 20, 11, 22, 3},
                {33, 50, 35, 54, 23, 18, 9, 12},
                {38, 57, 32, 45, 40, 21, 4, 17},
                {49, 34, 39, 24, 61, 8, 13, 26},
                {58, 31, 44, 41, 46, 25, 16, 5},
                {43, 48, 29, 60, 7, 62, 27, 14},
                {30, 59, 42, 47, 28, 15, 6, 63}
        };

        KnightTourValidateResponse response =
                knightTourService.validateSubmittedBoard(8, 0, 0, board);

        assertFalse(response.isCorrect());
    }

    @Test
    void shouldRejectBoardWithDuplicateMoveNumber() {
        int[][] board = {
                {51, 0, 55, 36, 53, 2, 19, 10},
                {56, 37, 52, 1, 20, 11, 22, 3},
                {33, 50, 35, 54, 23, 18, 9, 12},
                {38, 57, 32, 45, 40, 21, 4, 17},
                {49, 34, 39, 24, 61, 8, 13, 26},
                {58, 31, 44, 41, 46, 25, 16, 5},
                {43, 48, 29, 60, 7, 62, 27, 14},
                {30, 59, 42, 47, 28, 15, 6, 6}
        };

        KnightTourValidateResponse response =
                knightTourService.validateSubmittedBoard(8, 0, 1, board);

        assertFalse(response.isCorrect());
    }
}