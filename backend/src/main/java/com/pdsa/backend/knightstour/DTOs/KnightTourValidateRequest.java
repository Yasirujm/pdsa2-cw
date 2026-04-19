package com.pdsa.backend.knightstour.DTOs;

public class KnightTourValidateRequest {

    private int boardSize;
    private int startRow;
    private int startCol;
    private int[][] submittedBoard;

    public KnightTourValidateRequest() {
    }

    public KnightTourValidateRequest(int boardSize, int startRow, int startCol, int[][] submittedBoard) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.submittedBoard = submittedBoard;
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

    public int[][] getSubmittedBoard() {
        return submittedBoard;
    }

    public void setSubmittedBoard(int[][] submittedBoard) {
        this.submittedBoard = submittedBoard;
    }
}