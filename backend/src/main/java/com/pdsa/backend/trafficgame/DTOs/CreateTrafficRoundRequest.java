package com.pdsa.backend.trafficgame.DTOs;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class CreateTrafficRoundRequest {

    @Min(2)
    @Max(20)
    private int minCapacity = 5;

    @Min(2)
    @Max(30)
    private int maxCapacity = 15;

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
