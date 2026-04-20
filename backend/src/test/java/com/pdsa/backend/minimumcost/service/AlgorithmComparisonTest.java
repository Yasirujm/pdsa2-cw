package com.pdsa.backend.minimumcost.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmComparisonTest {
    private final HungarianSolverService hungarian = new HungarianSolverService();
    private final MinCostMaxFlowSolverService minCostFlow = new MinCostMaxFlowSolverService();

    @Test
    void bothAlgorithmsShouldReturnSameAnswer() {
        int[][] matrix = {
                {11, 7, 10, 17},
                {13, 21, 7, 11},
                {13, 13, 15, 13},
                {18, 10, 13, 16}
        };
        int a = hungarian.solve(matrix);
        int b = minCostFlow.solve(matrix);
        assertEquals(a, b);
    }
}
