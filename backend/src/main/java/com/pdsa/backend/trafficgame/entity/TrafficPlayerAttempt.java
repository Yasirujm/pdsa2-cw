package com.pdsa.backend.trafficgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_player_attempts")
public class TrafficPlayerAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private TrafficRound trafficRound;

    private String playerName;
    private int submittedFlow;
    private int actualMaxFlow;
    private boolean correct;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public TrafficRound getTrafficRound() {
        return trafficRound;
    }

    public void setTrafficRound(TrafficRound trafficRound) {
        this.trafficRound = trafficRound;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
