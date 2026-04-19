package com.pdsa.backend.knightstour.DTOs;


public class KnightTourStartRequest {

    private int boardSize;

    public KnightTourStartRequest() {
    }

    public KnightTourStartRequest(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }
}