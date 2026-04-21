package com.pdsa.backend.trafficgame.DTOs;

import java.time.LocalDateTime;

public class TrafficLeaderboardEntryResponse {

    private String playerName;
    private Long roundId;
    private int submittedFlow;
    private int actualMaxFlow;
    private boolean correct;
    private LocalDateTime playedAt;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public int getSubmittedFlow() {
        return submittedFlow;
    }

    public void setSubmittedFlow(int submittedFlow) {
        this.submittedFlow = submittedFlow;
    }

    public int getActualMaxFlow() {
        return actualMaxFlow;
    }

    public void setActualMaxFlow(int actualMaxFlow) {
        this.actualMaxFlow = actualMaxFlow;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }

    public void setPlayedAt(LocalDateTime playedAt) {
        this.playedAt = playedAt;
    }
}
