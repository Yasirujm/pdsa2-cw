package com.pdsa.backend.minimumcost.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class MinimumCostCreateRoundRequest {
    @Min(2)
    @Max(100)
    private int taskCount;

    private boolean useRandomSize;

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public boolean isUseRandomSize() {
        return useRandomSize;
    }

    public void setUseRandomSize(boolean useRandomSize) {
        this.useRandomSize = useRandomSize;
    }
}
