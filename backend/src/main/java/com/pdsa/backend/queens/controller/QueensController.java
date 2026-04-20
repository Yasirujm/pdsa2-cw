package com.pdsa.backend.queens.controller;

import com.pdsa.backend.queens.entity.AlgorithmRun;
import com.pdsa.backend.queens.entity.GameSession;
import com.pdsa.backend.queens.service.QueensGameService;
import com.pdsa.queens.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/queens")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class QueensController {

    private final QueensGameService gameService;

    /**
     * POST /api/queens/submit
     * Body: { "playerName": "Alice", "placement": [0,4,7,5,2,6,1,3,...] }
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitAnswer(@RequestBody Map<String, Object> body) {
        String playerName = (String) body.get("playerName");
        List<Integer> placementList = (List<Integer>) body.get("placement");
        int[] placement = placementList.stream().mapToInt(Integer::intValue).toArray();

        QueensGameService.GameResult result = gameService.submitAnswer(playerName, placement);
        return ResponseEntity.ok(Map.of("result", result.result(), "message", result.message()));
    }

    /**
     * GET /api/queens/leaderboard
     * Returns all correct submissions
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<GameSession>> getLeaderboard() {
        return ResponseEntity.ok(gameService.getLeaderboard());
    }

    /**
     * GET /api/queens/stats
     * Returns algorithm run timings for the report chart
     */
    @GetMapping("/stats")
    public ResponseEntity<List<AlgorithmRun>> getStats() {
        return ResponseEntity.ok(gameService.getAlgorithmStats());
    }
}