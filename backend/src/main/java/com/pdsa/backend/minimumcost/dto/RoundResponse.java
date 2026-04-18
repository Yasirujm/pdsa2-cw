package com.pdsa.backend.minimumcost.dto;

import java.util.List;

public class RoundResponse {
    private Long gameRoundId;
    private int taskCount;
    private int[][] costMatrix;
    private List<Integer> answerChoices;
    private int hungarianAnswer;
    private int minCostFlowAnswer;
    private long hungarianTimeNanos;
    private long minCostFlowTimeNanos;
    public Long getGameRoundId() { return gameRoundId; }

    public void setGameRoundId(Long gameRoundId) {
        this.gameRoundId = gameRoundId;
    }

    public int getTaskCount() { return taskCount; }

    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

    public int[][] getCostMatrix() { return costMatrix; }

    public void setCostMatrix(int[][] costMatrix) {
        this.costMatrix = costMatrix;
    }

    public List<Integer> getAnswerChoices() { return answerChoices; }

    public void setAnswerChoices(List<Integer> answerChoices) {
        this.answerChoices = answerChoices;
    }

    public int getHungarianAnswer() { return hungarianAnswer; }

    public void setHungarianAnswer(int hungarianAnswer) {
        this.hungarianAnswer = hungarianAnswer;
    }

    public int getMinCostFlowAnswer() { return minCostFlowAnswer; }

    public void setMinCostFlowAnswer(int minCostFlowAnswer) {
        this.minCostFlowAnswer = minCostFlowAnswer;
    }

    public long getHungarianTimeNanos() { return hungarianTimeNanos; }

    public void setHungarianTimeNanos(long hungarianTimeNanos) {
        this.hungarianTimeNanos = hungarianTimeNanos;
    }

    public long getMinCostFlowTimeNanos() { return minCostFlowTimeNanos; }

    public void setMinCostFlowTimeNanos(long minCostFlowTimeNanos) {
        this.minCostFlowTimeNanos = minCostFlowTimeNanos;
    }

}

