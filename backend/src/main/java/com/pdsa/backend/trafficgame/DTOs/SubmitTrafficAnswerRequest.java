package com.pdsa.backend.trafficgame.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmitTrafficAnswerRequest {

    @NotNull
    private Long roundId;

    @NotBlank
    private String playerName;

    @Min(0)
    private int estimatedMaxFlow;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getEstimatedMaxFlow() {
        return estimatedMaxFlow;
    }

    public void setEstimatedMaxFlow(int estimatedMaxFlow) {
        this.estimatedMaxFlow = estimatedMaxFlow;
    }
}
