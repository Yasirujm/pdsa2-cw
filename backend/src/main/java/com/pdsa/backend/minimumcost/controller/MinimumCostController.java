package com.pdsa.backend.minimumcost.controller;

import com.pdsa.backend.minimumcost.dto.MinimumCostCreateRoundRequest;
import com.pdsa.backend.minimumcost.dto.MinimumCostRoundResponse;
import com.pdsa.backend.minimumcost.dto.MinimumCostSubmitAnswerRequest;
import com.pdsa.backend.minimumcost.dto.MinimumCostSubmitAnswerResponse;
import com.pdsa.backend.minimumcost.service.MinimumCostGameService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/minimum-cost")
@CrossOrigin(origins = "http://localhost:3000")
public class MinimumCostController {
    private final MinimumCostGameService gameService;
    public MinimumCostController(MinimumCostGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/round")
    public ResponseEntity<MinimumCostRoundResponse> createRound(@Valid @RequestBody MinimumCostCreateRoundRequest request) {
        return ResponseEntity.ok(gameService.createRound(request.getTaskCount()));
    }

    @PostMapping("/answer")
    public ResponseEntity<MinimumCostSubmitAnswerResponse> submitAnswer(@Valid @RequestBody MinimumCostSubmitAnswerRequest request) {
        return ResponseEntity.ok(gameService.submitAnswer(request));
    }
}
