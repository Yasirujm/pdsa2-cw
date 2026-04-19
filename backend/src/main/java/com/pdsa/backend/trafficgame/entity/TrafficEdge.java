package com.pdsa.backend.trafficgame.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "traffic_edges")
public class TrafficEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromNode;
    private String toNode;
    private int capacity;
    private int flow;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private TrafficRound trafficRound;

    public TrafficEdge() {
    }

    public TrafficEdge(String fromNode, String toNode, int capacity, int flow) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.capacity = capacity;
        this.flow = flow;
    }

    public Long getId() {
        return id;
    }

    public String getFromNode() {
        return fromNode;
    }

    public void setFromNode(String fromNode) {
        this.fromNode = fromNode;
    }

    public String getToNode() {
        return toNode;
    }

    public void setToNode(String toNode) {
        this.toNode = toNode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public TrafficRound getTrafficRound() {
        return trafficRound;
    }

    public void setTrafficRound(TrafficRound trafficRound) {
        this.trafficRound = trafficRound;
    }
}
