package com.pdsa.backend.knightstour.DTOs;

public class KnightTourStartResponse {

    private int boardSize;
    private int startRow;
    private int startCol;
    private String message;

    public KnightTourStartResponse() {
    }

    public KnightTourStartResponse(int boardSize, int startRow, int startCol, String message) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}