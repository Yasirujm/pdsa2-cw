package com.pdsa.backend.knightstour.DTOs;

public class KnightTourGreedySolveResponse {

    private int boardSize;
    private int startRow;
    private int startCol;
    private long greedyTimeTaken;
    private int[][] greedyBoard;

    public KnightTourGreedySolveResponse() {
    }

    public KnightTourGreedySolveResponse(
            int boardSize,
            int startRow,
            int startCol,
            long greedyTimeTaken,
            int[][] greedyBoard
    ) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.greedyTimeTaken = greedyTimeTaken;
        this.greedyBoard = greedyBoard;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public long getGreedyTimeTaken() {
        return greedyTimeTaken;
    }

    public int[][] getGreedyBoard() {
        return greedyBoard;
    }
}