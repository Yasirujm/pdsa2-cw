package com.pdsa.backend.knightstour.DTOs;

public class KnightTourSolveResponse {

    private int boardSize;
    private int startRow;
    private int startCol;
    private long dfsTimeTaken;
    private long greedyTimeTaken;
    private int[][] dfsBoard;
    private int[][] greedyBoard;

    public KnightTourSolveResponse() {
    }

    public KnightTourSolveResponse(
            int boardSize,
            int startRow,
            int startCol,
            long dfsTimeTaken,
            long greedyTimeTaken,
            int[][] dfsBoard,
            int[][] greedyBoard
    ) {
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.dfsTimeTaken = dfsTimeTaken;
        this.greedyTimeTaken = greedyTimeTaken;
        this.dfsBoard = dfsBoard;
        this.greedyBoard = greedyBoard;
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

    public long getDfsTimeTaken() {
        return dfsTimeTaken;
    }

    public void setDfsTimeTaken(long dfsTimeTaken) {
        this.dfsTimeTaken = dfsTimeTaken;
    }

    public long getGreedyTimeTaken() {
        return greedyTimeTaken;
    }

    public void setGreedyTimeTaken(long greedyTimeTaken) {
        this.greedyTimeTaken = greedyTimeTaken;
    }

    public int[][] getDfsBoard() {
        return dfsBoard;
    }

    public void setDfsBoard(int[][] dfsBoard) {
        this.dfsBoard = dfsBoard;
    }

    public int[][] getGreedyBoard() {
        return greedyBoard;
    }

    public void setGreedyBoard(int[][] greedyBoard) {
        this.greedyBoard = greedyBoard;
    }
}