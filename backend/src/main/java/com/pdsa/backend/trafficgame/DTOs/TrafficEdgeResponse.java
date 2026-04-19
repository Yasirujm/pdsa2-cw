package com.pdsa.backend.trafficgame.DTOs;

public class TrafficEdgeResponse {

    private String from;
    private String to;
    private int capacity;
    private int flow;

    public TrafficEdgeResponse() {
    }

    public TrafficEdgeResponse(String from, String to, int capacity, int flow) {
        this.from = from;
        this.to = to;
        this.capacity = capacity;
        this.flow = flow;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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
}
