package com.pdsa.backend.trafficgame.DTOs;

import java.time.LocalDateTime;
import java.util.List;

public class TrafficRoundDetailsResponse {

    private Long roundId;
    private String source;
    private String sink;
    private List<String> nodes;
    private List<TrafficEdgeResponse> edges;
    private int maxFlowEdmondsKarp;
    private int maxFlowFordFulkerson;
    private long edmondsKarpTimeNanos;
    private long fordFulkersonTimeNanos;
    private LocalDateTime createdAt;

    public Long getRoundId() {
        return roundId;
    }

    public void setRoundId(Long roundId) {
        this.roundId = roundId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSink() {
        return sink;
    }

    public void setSink(String sink) {
        this.sink = sink;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public List<TrafficEdgeResponse> getEdges() {
        return edges;
    }

    public void setEdges(List<TrafficEdgeResponse> edges) {
        this.edges = edges;
    }

    public int getMaxFlowEdmondsKarp() {
        return maxFlowEdmondsKarp;
    }

    public void setMaxFlowEdmondsKarp(int maxFlowEdmondsKarp) {
        this.maxFlowEdmondsKarp = maxFlowEdmondsKarp;
    }

    public int getMaxFlowFordFulkerson() {
        return maxFlowFordFulkerson;
    }

    public void setMaxFlowFordFulkerson(int maxFlowFordFulkerson) {
        this.maxFlowFordFulkerson = maxFlowFordFulkerson;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
