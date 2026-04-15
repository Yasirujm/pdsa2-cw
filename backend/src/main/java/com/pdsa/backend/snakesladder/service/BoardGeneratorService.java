package com.pdsa.backend.snakesladder.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BoardGeneratorService {
    public Map<Integer, Integer> generateJumps(int boardSize) {
        int totalCells = boardSize * boardSize;
        int count = boardSize - 2;

        Map<Integer, Integer> jumps = new HashMap<>();
        Set<Integer> usedStarts = new HashSet<>();
        Set<Integer> usedEnds = new HashSet<>();
        Random random = new Random();

        int laddersCreated = 0;

        while (laddersCreated < count) {
            int start = 2 + random.nextInt(totalCells - 2);
            int end = 2 + random.nextInt(totalCells - 2);
            if (start >= end) continue;
            if (usedStarts.contains(start) || usedEnds.contains(end)) continue;
            if (jumps.containsKey(start) || jumps.containsKey(end)) continue;
            jumps.put(start, end);
            usedStarts.add(start);
            usedEnds.add(end);
            laddersCreated++;
        }

        int snakesCreated = 0;

        while (snakesCreated < count) {
            int start = 2 + random.nextInt(totalCells - 2);
            int end = 2 + random.nextInt(totalCells - 2);
            if (start <= end) continue;
            if (usedStarts.contains(start) || usedEnds.contains(end)) continue;
            if (jumps.containsKey(start) || jumps.containsKey(end)) continue;
            jumps.put(start, end);
            usedStarts.add(start);
            usedEnds.add(end);
            snakesCreated++;
        }

        return jumps;
    }
}

