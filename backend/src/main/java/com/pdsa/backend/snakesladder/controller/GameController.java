package com.pdsa.backend.snakesladder.controller;

import com.pdsa.backend.snakesladder.DTOs.CreateRoundRequest;
import com.pdsa.backend.snakesladder.DTOs.RoundResponse;
import com.pdsa.backend.snakesladder.DTOs.SubmitAnswerRequest;
import com.pdsa.backend.snakesladder.DTOs.SubmitAnswerResponse;
import com.pdsa.backend.snakesladder.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/snakes-ladder")
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/round")
    public ResponseEntity<RoundResponse> createRound(@Valid @RequestBody CreateRoundRequest request) {
        return ResponseEntity.ok(gameService.createRound(request.getBoardSize()));
    }

    @PostMapping("/answer")
    public ResponseEntity<SubmitAnswerResponse> submitAnswer(@Valid @RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(gameService.submitAnswer(request));
    }
}
