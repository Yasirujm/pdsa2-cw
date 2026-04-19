package com.pdsa.backend.knightstour.controller;

import com.pdsa.backend.knightstour.DTOs.KnightTourStartRequest;
import com.pdsa.backend.knightstour.DTOs.KnightTourStartResponse;
import com.pdsa.backend.knightstour.service.KnightTourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pdsa.backend.knightstour.DTOs.KnightTourSolveRequest;
import com.pdsa.backend.knightstour.DTOs.KnightTourSolveResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourValidateRequest;
import com.pdsa.backend.knightstour.DTOs.KnightTourValidateResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourSaveResultRequest;
import com.pdsa.backend.knightstour.DTOs.KnightTourSaveResultResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourLeaderboardResponse;
import java.util.List;
import com.pdsa.backend.knightstour.DTOs.KnightTourGreedySolveResponse;
import com.pdsa.backend.knightstour.DTOs.KnightTourDfsSolveResponse;

@RestController
@RequestMapping("/api/knight-tour")
@CrossOrigin
public class KnightTourController {

    private final KnightTourService knightTourService;

    public KnightTourController(KnightTourService knightTourService) {
        this.knightTourService = knightTourService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startGame(@RequestBody KnightTourStartRequest request) {
        try {
            KnightTourStartResponse response = knightTourService.startGame(request.getBoardSize());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/solve")
    public ResponseEntity<?> solveGame(@RequestBody KnightTourSolveRequest request) {
        try {
            KnightTourSolveResponse response = knightTourService.solveGame(
                    request.getBoardSize(),
                    request.getStartRow(),
                    request.getStartCol()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/solve-greedy")
    public ResponseEntity<?> solveGreedy(@RequestBody KnightTourSolveRequest request) {
        try {
            KnightTourGreedySolveResponse response = knightTourService.solveGreedyOnly(
                    request.getBoardSize(),
                    request.getStartRow(),
                    request.getStartCol()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/solve-dfs")
    public ResponseEntity<?> solveDfs(@RequestBody KnightTourSolveRequest request) {
        try {
            KnightTourDfsSolveResponse response = knightTourService.solveDfsOnly(
                    request.getBoardSize(),
                    request.getStartRow(),
                    request.getStartCol()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateBoard(@RequestBody KnightTourValidateRequest request) {
        try {
            KnightTourValidateResponse response = knightTourService.validateSubmittedBoard(
                    request.getBoardSize(),
                    request.getStartRow(),
                    request.getStartCol(),
                    request.getSubmittedBoard()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/save-result")
    public ResponseEntity<?> saveGameResult(@RequestBody KnightTourSaveResultRequest request) {
        try {
            KnightTourSaveResultResponse response = knightTourService.saveGameResult(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<List<KnightTourLeaderboardResponse>> getLeaderboard() {
        return ResponseEntity.ok(knightTourService.getLeaderboard());
    }
}