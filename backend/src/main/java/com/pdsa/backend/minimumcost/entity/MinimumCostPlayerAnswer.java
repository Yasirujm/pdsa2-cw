package com.pdsa.backend.minimumcost.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "minimum_cost_player_answers")
public class MinimumCostPlayerAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_round_id", nullable = false)
    private MinimumCostRound gameRound;
    private String playerName;
    private int selectedAnswer;
    private int correctAnswer;
    private boolean isCorrect;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public MinimumCostRound getGameRound() { return gameRound; }

    public void setGameRound(MinimumCostRound gameRound) {
        this.gameRound = gameRound;
    }

    public String getPlayerName() { return playerName; }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getSelectedAnswer() { return selectedAnswer; }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public int getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() { return isCorrect; }

    public void setCorrect(boolean correct) { isCorrect = correct; }
}

