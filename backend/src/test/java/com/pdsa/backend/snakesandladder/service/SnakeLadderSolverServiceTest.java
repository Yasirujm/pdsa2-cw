package com.pdsa.backend.snakesandladder.service;

import com.pdsa.backend.snakesladder.service.SnakeLadderSolverService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SnakeLadderSolverServiceTest {

    private final SnakeLadderSolverService service = new SnakeLadderSolverService();

    @Test
    void bfsAndDijkstraShouldReturnSameAnswer() {
        Map<Integer, Integer> jumps = new HashMap<>();

        // sample snakes & ladders
        jumps.put(2, 15);  // ladder
        jumps.put(18, 5);  // snake
        jumps.put(9, 27);  // ladder
        jumps.put(25, 7);  // snake

        int bfs = service.minThrowsBfs(6, jumps);
        int dijkstra = service.minThrowsDijkstra(6, jumps);

        assertEquals(bfs, dijkstra);
    }
}