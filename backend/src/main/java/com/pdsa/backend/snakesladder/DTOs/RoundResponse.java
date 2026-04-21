package com.pdsa.backend.snakesladder.DTOs;

import java.util.List;
import java.util.Map;

public class RoundResponse {
    private Long gameRoundId;
    private int boardSize;
    private int totalCells;
    private Map<Integer, Integer> jumps;
    private List<Integer> answerChoices;
    private int bfsAnswer;
    private int dijkstraAnswer;
    private long bfsTimeNanos;
    private long dijkstraTimeNanos;

    public Long getGameRoundId() { return gameRoundId; }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public int getBoardSize() { return boardSize; }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getTotalCells() { return totalCells; }

    public void setTotalCells(int totalCells) {
        this.totalCells = totalCells;
    }

    public Map<Integer, Integer> getJumps() { return jumps; }

    public void setJumps(Map<Integer, Integer> jumps) {
        this.jumps = jumps;
    }

    public List<Integer> getAnswerChoices() { return answerChoices; }

    public void setAnswerChoices(List<Integer> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public int getBfsAnswer() { return bfsAnswer; }

    public void setBfsAnswer(int bfsAnswer) {
        this.bfsAnswer = bfsAnswer;
    }
    public int getDijkstraAnswer() { return dijkstraAnswer; }

    public void setDijkstraAnswer(int dijkstraAnswer) {
        this.dijkstraAnswer = dijkstraAnswer;
    }

    public long getBfsTimeNanos() { return bfsTimeNanos; }

    public void setBfsTimeNanos(long bfsTimeNanos) {
        this.bfsTimeNanos = bfsTimeNanos;
    }

    public long getDijkstraTimeNanos() { return dijkstraTimeNanos; }

    public void setDijkstraTimeNanos(long dijkstraTimeNanos) {
        this.dijkstraTimeNanos = dijkstraTimeNanos;
    }
}

