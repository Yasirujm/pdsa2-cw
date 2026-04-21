package com.pdsa.backend.snakesladder.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class CreateRoundRequest {
    @Min(6)
    @Max(12)
    private int boardSize;
    public int getBoardSize() { return boardSize; }
    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }
}

