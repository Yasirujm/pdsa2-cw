package com.pdsa.backend.trafficgame.service;

import com.pdsa.backend.trafficgame.DTOs.TrafficEdgeResponse;
import com.pdsa.backend.trafficgame.service.TrafficFlowAlgorithmService.MaxFlowResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficFlowAlgorithmServiceTest {

    private TrafficFlowAlgorithmService trafficService;

    @BeforeEach
    void setUp() {
        trafficService = new TrafficFlowAlgorithmService();
    }

    @Test
    void testEdmondsKarpAlgorithm_CorrectMaxFlow() {
        // Arrange: A small fixed graph. Total capacity from A -> T needs to be verifiable.
        // Paths: A->B (10), B->T (5), A->C (15), C->T (15)
        // Assume graph maps directly across A,B,C,T which are all valid nodes inside the array.
        List<TrafficEdgeResponse> mockEdges = new ArrayList<>();
        mockEdges.add(new TrafficEdgeResponse("A", "B", 10, 0));
        mockEdges.add(new TrafficEdgeResponse("B", "T", 5, 0));
        mockEdges.add(new TrafficEdgeResponse("A", "C", 15, 0));
        mockEdges.add(new TrafficEdgeResponse("C", "T", 15, 0));
        
        // Let's add standard unused edges as 0 just in case logic depends on it
        // The service logic builds matrix for all array nodes based on these inputs.
        
        // Act
        MaxFlowResult result = trafficService.solveWithEdmondsKarp(mockEdges, "A", "T");

        // Assert
        // A -> B limits at 5 (due to B->T), A->C flows fully at 15. Max = 20.
        assertNotNull(result);
        assertEquals(20, result.getMaxFlow(), "Edmonds-Karp max flow should be exactly 20");
    }

    @Test
    void testFordFulkersonAlgorithm_CorrectMaxFlow() {
        // Arrange
        List<TrafficEdgeResponse> mockEdges = new ArrayList<>();
        mockEdges.add(new TrafficEdgeResponse("A", "B", 10, 0));
        mockEdges.add(new TrafficEdgeResponse("B", "T", 5, 0));
        mockEdges.add(new TrafficEdgeResponse("A", "C", 15, 0));
        mockEdges.add(new TrafficEdgeResponse("C", "T", 15, 0));

        // Act
        MaxFlowResult result = trafficService.solveWithFordFulkerson(mockEdges, "A", "T");

        // Assert
        assertNotNull(result);
        assertEquals(20, result.getMaxFlow(), "Ford-Fulkerson max flow should be exactly 20");
    }

    @Test
    void testAlgorithmsProduceSameOutput() {
        // Arrange: Using the service's random edge generator
        List<TrafficEdgeResponse> randomEdges = trafficService.generateRandomEdges(10, 50);

        // Act
        MaxFlowResult edmondsResult = trafficService.solveWithEdmondsKarp(randomEdges, "A", "T");
        MaxFlowResult fordResult = trafficService.solveWithFordFulkerson(randomEdges, "A", "T");

        // Assert
        assertEquals(edmondsResult.getMaxFlow(), fordResult.getMaxFlow(), 
                "Both algorithms must compute the exact same Maximum Flow");
        
        // Verify time metrics were calculated
        assertTrue(edmondsResult.getTimeNanos() > -1);
        assertTrue(fordResult.getTimeNanos() > -1);
    }
}
