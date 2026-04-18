package com.pdsa.backend.minimumcost.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "minimum_cost_entries")
public class CostEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int taskIndex;
    private int employeeIndex;
    private int costValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_round_id", nullable = false)
    private MinimumCostRound gameRound;

    public CostEntry() {}
    public CostEntry(int taskIndex, int employeeIndex, int costValue) {
        this.taskIndex = taskIndex;
        this.employeeIndex = employeeIndex;
        this.costValue = costValue;
    }

    public Long getId() { return id; }

    public int getTaskIndex() { return taskIndex; }

    public void setTaskIndex(int taskIndex) { this.taskIndex = taskIndex; }

    public int getEmployeeIndex() { return employeeIndex; }

    public void setEmployeeIndex(int employeeIndex) {
        this.employeeIndex = employeeIndex;
    }
    public int getCostValue() { return costValue; }

    public void setCostValue(int costValue) { this.costValue = costValue; }

    public MinimumCostRound getGameRound() { return gameRound; }

    public void setGameRound(MinimumCostRound gameRound) { this.gameRound = gameRound; }
}