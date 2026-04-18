package com.pdsa.backend.minimumcost.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class MinimumCostCreateRoundRequest {
    @Min(50)
    @Max(100)
    private int taskCount;
    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }
}
