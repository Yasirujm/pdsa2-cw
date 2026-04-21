package com.pdsa.backend.trafficgame.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traffic_rounds")
public class TrafficRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceNode;
    private String sinkNode;
    private int maxFlowEdmondsKarp;
    private int maxFlowFordFulkerson;
    private long edmondsKarpTimeNanos;
    private long fordFulkersonTimeNanos;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "trafficRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrafficEdge> edges = new ArrayList<>();

    @OneToMany(mappedBy = "trafficRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrafficPlayerAttempt> attempts = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void addEdge(TrafficEdge edge) {
        edges.add(edge);
        edge.setTrafficRound(this);
    }

    public void addAttempt(TrafficPlayerAttempt attempt) {
        attempts.add(attempt);
        attempt.setTrafficRound(this);
    }

    public Long getId() {
        return id;
    }

    public String getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(String sourceNode) {
        this.sourceNode = sourceNode;
    }

    public String getSinkNode() {
        return sinkNode;
    }

    public void setSinkNode(String sinkNode) {
        this.sinkNode = sinkNode;
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

    public List<TrafficEdge> getEdges() {
        return edges;
    }

    public List<TrafficPlayerAttempt> getAttempts() {
        return attempts;
    }
}
