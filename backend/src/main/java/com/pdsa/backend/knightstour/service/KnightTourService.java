package com.pdsa.backend.knightstour.service;

import com.pdsa.backend.knightstour.DTOs.KnightTourResult;
import org.springframework.stereotype.Service;
import com.pdsa.backend.knightstour.DTOs.KnightTourStartResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourSolveResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourValidateResponse;

import com.pdsa.backend.knightstour.DTOs.KnightTourSaveResultRequest;
import com.pdsa.backend.knightstour.DTOs.KnightTourSaveResultResponse;
import com.pdsa.backend.knightstour.entity.KnightTourGameResult;
import com.pdsa.backend.knightstour.repositories.KnightTourGameResultRepository;
import java.time.LocalDateTime;
import com.pdsa.backend.knightstour.DTOs.KnightTourLeaderboardResponse;
import java.util.List;
import java.util.stream.Collectors;
import com.pdsa.backend.knightstour.DTOs.KnightTourGreedySolveResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourDfsSolveResponse;

@Service
public class KnightTourService {

    public KnightTourService(KnightTourGameResultRepository knightTourGameResultRepository) {
        this.knightTourGameResultRepository = knightTourGameResultRepository;
    }


    private final KnightTourGameResultRepository knightTourGameResultRepository;

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

    public KnightTourStartResponse startGame(int size) {
        validateBoardSize(size);

        int[] start = generateRandomStart(size);
        int startRow = start[0];
        int startCol = start[1];

        return new KnightTourStartResponse(
                size,
                startRow,
                startCol,
                "Game started successfully"
        );
    }

    private void validateStartPosition(int size, int startRow, int startCol) {
        if (startRow < 0 || startRow >= size || startCol < 0 || startCol >= size) {
            throw new IllegalArgumentException("Invalid start position for board size " + size);
        }
    }

    public KnightTourSolveResponse solveGame(int size, int startRow, int startCol) {
        validateBoardSize(size);
        validateStartPosition(size, startRow, startCol);

        long dfsStartTime = System.currentTimeMillis();
        int[][] dfsBoard = solveWithDFS(size, startRow, startCol);
        long dfsEndTime = System.currentTimeMillis();

        long greedyStartTime = System.currentTimeMillis();
        int[][] greedyBoard;

        try {
            greedyBoard = solveWithGreedy(size, startRow, startCol);
        } catch (RuntimeException e) {
            greedyBoard = solveWithGreedy(size, 0, 0);
            startRow = 0;
            startCol = 0;
        }

        long greedyEndTime = System.currentTimeMillis();

        return new KnightTourSolveResponse(
                size,
                startRow,
                startCol,
                dfsEndTime - dfsStartTime,
                greedyEndTime - greedyStartTime,
                dfsBoard,
                greedyBoard
        );
    }

    private boolean isKnightMove(int currentRow, int currentCol, int nextRow, int nextCol) {
        int rowDiff = Math.abs(currentRow - nextRow);
        int colDiff = Math.abs(currentCol - nextCol);

        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }

    private int[][] buildMovePositions(int[][] board, int size) {
        int totalMoves = size * size;
        int[][] positions = new int[totalMoves][2];
        boolean[] seen = new boolean[totalMoves];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board[row][col];

                if (value < 0 || value >= totalMoves) {
                    throw new IllegalArgumentException("Board contains invalid value: " + value);
                }

                if (seen[value]) {
                    throw new IllegalArgumentException("Duplicate move number found: " + value);
                }

                seen[value] = true;
                positions[value][0] = row;
                positions[value][1] = col;
            }
        }

        for (int i = 0; i < totalMoves; i++) {
            if (!seen[i]) {
                throw new IllegalArgumentException("Missing move number: " + i);
            }
        }

        return positions;
    }

    private void validateSubmittedBoardShape(int[][] board, int size) {
        if (board == null || board.length != size) {
            throw new IllegalArgumentException("Submitted board must have " + size + " rows");
        }

        for (int[] row : board) {
            if (row == null || row.length != size) {
                throw new IllegalArgumentException("Submitted board must be a " + size + " x " + size + " grid");
            }
        }
    }

    public KnightTourValidateResponse validateSubmittedBoard(int size, int startRow, int startCol, int[][] submittedBoard) {
        validateBoardSize(size);
        validateStartPosition(size, startRow, startCol);
        validateSubmittedBoardShape(submittedBoard, size);

        try {
            int[][] positions = buildMovePositions(submittedBoard, size);

            if (positions[0][0] != startRow || positions[0][1] != startCol) {
                return new KnightTourValidateResponse(false, "Starting position does not match the required start cell");
            }

            int totalMoves = size * size;

            for (int move = 0; move < totalMoves - 1; move++) {
                int currentRow = positions[move][0];
                int currentCol = positions[move][1];
                int nextRow = positions[move + 1][0];
                int nextCol = positions[move + 1][1];

                if (!isKnightMove(currentRow, currentCol, nextRow, nextCol)) {
                    return new KnightTourValidateResponse(
                            false,
                            "Invalid knight move from " + move + " to " + (move + 1)
                    );
                }
            }

            return new KnightTourValidateResponse(true, "Valid Knight's Tour");
        } catch (IllegalArgumentException e) {
            return new KnightTourValidateResponse(false, e.getMessage());
        }
    }

    public KnightTourSaveResultResponse saveGameResult(KnightTourSaveResultRequest request) {
        if (request.getPlayerName() == null || request.getPlayerName().trim().isEmpty()) {
            throw new IllegalArgumentException("Player name is required");
        }

        KnightTourGameResult result = new KnightTourGameResult(
                request.getPlayerName().trim(),
                request.getBoardSize(),
                request.getStartRow(),
                request.getStartCol(),
                request.isCompleted(),
                request.getMoveCount(),
                request.getTimeTakenMillis(),
                request.getValidationMessage(),
                LocalDateTime.now()
        );

        KnightTourGameResult saved = knightTourGameResultRepository.save(result);

        return new KnightTourSaveResultResponse(
                saved.getId(),
                "Game result saved successfully"
        );
    }

    public List<KnightTourLeaderboardResponse> getLeaderboard() {
        return knightTourGameResultRepository
                .findAllByOrderByCompletedDescTimeTakenMillisAscMoveCountDescCreatedAtDesc()
                .stream()
                .map(result -> new KnightTourLeaderboardResponse(
                        result.getId(),
                        result.getPlayerName(),
                        result.getBoardSize(),
                        result.isCompleted(),
                        result.getMoveCount(),
                        result.getTimeTakenMillis(),
                        result.getValidationMessage(),
                        result.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public KnightTourGreedySolveResponse solveGreedyOnly(int size, int startRow, int startCol) {
        validateBoardSize(size);
        validateStartPosition(size, startRow, startCol);

        long startTime = System.currentTimeMillis();
        int[][] greedyBoard = solveWithGreedy(size, startRow, startCol);
        long endTime = System.currentTimeMillis();

        return new KnightTourGreedySolveResponse(
                size,
                startRow,
                startCol,
                endTime - startTime,
                greedyBoard
        );
    }

    public KnightTourDfsSolveResponse solveDfsOnly(int size, int startRow, int startCol) {
        validateBoardSize(size);
        validateStartPosition(size, startRow, startCol);

        long startTime = System.currentTimeMillis();
        int[][] dfsBoard = solveWithDFS(size, startRow, startCol);
        long endTime = System.currentTimeMillis();

        return new KnightTourDfsSolveResponse(
                size,
                startRow,
                startCol,
                endTime - startTime,
                dfsBoard
        );
    }

}