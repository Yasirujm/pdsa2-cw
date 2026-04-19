package com.pdsa.backend.knightstour.DTOs;

public class KnightTourDfsSolveResponse {

    private int boardSize;
    private int startRow;
    private int startCol;
    private long dfsTimeTaken;
    private int[][] dfsBoard;

    public KnightTourDfsSolveResponse() {
    }

    public KnightTourDfsSolveResponse(
            int boardSize,
            int startRow,
            int startCol,
            long dfsTimeTaken,
            int[][] dfsBoard
    ) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.dfsTimeTaken = dfsTimeTaken;
        this.dfsBoard = dfsBoard;
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

    public long getDfsTimeTaken() {
        return dfsTimeTaken;
    }

    public int[][] getDfsBoard() {
        return dfsBoard;
    }
}