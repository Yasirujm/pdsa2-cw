package com.pdsa.backend.snakesladder.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmitAnswerRequest {
    @NotBlank
    private String playerName;
    @NotNull
    private Long gameRoundId;
    @NotNull
    private Integer selectedAnswer;
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName =
            playerName; }
    public Long getGameRoundId() { return gameRoundId; }
    public void setGameRoundId(Long gameRoundId) { this.gameRoundId =
            gameRoundId; }
    public Integer getSelectedAnswer() { return selectedAnswer; }
    public void setSelectedAnswer(Integer selectedAnswer) { this.selectedAnswer
            = selectedAnswer; }
}
