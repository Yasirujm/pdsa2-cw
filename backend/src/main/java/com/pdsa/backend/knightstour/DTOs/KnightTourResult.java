package com.pdsa.backend.knightstour.DTOs;


public class KnightTourResult {

    private int[][] board;
    private int startRow;
    private int startCol;
    private long timeTaken;

    public KnightTourResult(int[][] board, int startRow, int startCol, long timeTaken) {
        this.board = board;
        this.startRow = startRow;
        this.startCol = startCol;
        this.timeTaken = timeTaken;
    }

    public int[][] getBoard() { return board; }
    public int getStartRow() { return startRow; }
    public int getStartCol() { return startCol; }
    public long getTimeTaken() { return timeTaken; }
}
