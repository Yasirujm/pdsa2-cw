package com.pdsa.backend.knightstour.DTOs;

import java.time.LocalDateTime;

public class KnightTourLeaderboardResponse {

    private Long id;
    private String playerName;
    private int boardSize;
    private boolean completed;
    private int moveCount;
    private long timeTakenMillis;
    private String validationMessage;
    private LocalDateTime createdAt;

    public KnightTourLeaderboardResponse() {
    }

    public KnightTourLeaderboardResponse(
            Long id,
            String playerName,
            int boardSize,
            boolean completed,
            int moveCount,
            long timeTakenMillis,
            String validationMessage,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.playerName = playerName;
        this.boardSize = boardSize;
        this.completed = completed;
        this.moveCount = moveCount;
        this.timeTakenMillis = timeTakenMillis;
        this.validationMessage = validationMessage;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public long getTimeTakenMillis() {
        return timeTakenMillis;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}