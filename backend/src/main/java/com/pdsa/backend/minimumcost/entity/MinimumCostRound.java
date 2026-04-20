package com.pdsa.backend.minimumcost.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "minimum_cost_rounds")
public class MinimumCostRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int taskCount;
    private int hungarianMinCost;
    private int minCostFlowMinCost;
    private long hungarianTimeNanos;
    private long minCostFlowTimeNanos;
    private int correctAnswer;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "gameRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CostEntry> costs = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public void addCost(CostEntry costEntry) {
        costs.add(costEntry);
        costEntry.setGameRound(this);
    }

    public Long getId() { return id; }

    public int getTaskCount() { return taskCount; }

    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

    public int getHungarianMinCost() { return hungarianMinCost; }

    public void setHungarianMinCost(int hungarianMinCost) {
        this.hungarianMinCost = hungarianMinCost;
    }

    public int getMinCostFlowMinCost() { return minCostFlowMinCost; }

    public void setMinCostFlowMinCost(int minCostFlowMinCost) {
        this.minCostFlowMinCost = minCostFlowMinCost;
    }

    public long getHungarianTimeNanos() { return hungarianTimeNanos; }

    public void setHungarianTimeNanos(long hungarianTimeNanos) {
        this.hungarianTimeNanos = hungarianTimeNanos;
    }

    public long getMinCostFlowTimeNanos() { return minCostFlowTimeNanos; }

    public void setMinCostFlowTimeNanos(long minCostFlowTimeNanos) {
        this.minCostFlowTimeNanos = minCostFlowTimeNanos;
    }

    public int getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<CostEntry> getCosts() { return costs; }
}

