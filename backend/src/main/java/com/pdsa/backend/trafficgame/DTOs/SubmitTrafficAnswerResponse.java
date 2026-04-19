package com.pdsa.backend.trafficgame.DTOs;

import java.util.List;

public class SubmitTrafficAnswerResponse {

    private Long attemptId;
    private boolean correct;
    private int submittedFlow;
    private int actualMaxFlow;
    private long edmondsKarpTimeNanos;
    private long fordFulkersonTimeNanos;
    private List<TrafficEdgeResponse> edgeFlows;

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
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

    public long getEdmondsKarpTimeNanos() {
        return edmondsKarpTimeNanos;
    }

    public void setEdmondsKarpTimeNanos(long edmondsKarpTimeNanos) {
        this.edmondsKarpTimeNanos = edmondsKarpTimeNanos;
    }

    public long getFordFulkersonTimeNanos() {
        return fordFulkersonTimeNanos;
    }

    public void setFordFulkersonTimeNanos(long fordFulkersonTimeNanos) {
        this.fordFulkersonTimeNanos = fordFulkersonTimeNanos;
    }

    public List<TrafficEdgeResponse> getEdgeFlows() {
        return edgeFlows;
    }

    public void setEdgeFlows(List<TrafficEdgeResponse> edgeFlows) {
        this.edgeFlows = edgeFlows;
    }
}
