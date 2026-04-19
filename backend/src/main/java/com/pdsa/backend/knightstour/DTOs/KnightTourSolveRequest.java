package com.pdsa.backend.knightstour.DTOs;

public class KnightTourSolveRequest {

    private int boardSize;
    private int startRow;
    private int startCol;

    public KnightTourSolveRequest() {
    }

    public KnightTourSolveRequest(int boardSize, int startRow, int startCol) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }
}