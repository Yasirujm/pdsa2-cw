package com.pdsa.backend.trafficgame.controller;

import com.pdsa.backend.trafficgame.DTOs.CreateTrafficRoundRequest;
import com.pdsa.backend.trafficgame.DTOs.CreateTrafficRoundResponse;
import com.pdsa.backend.trafficgame.DTOs.SubmitTrafficAnswerRequest;
import com.pdsa.backend.trafficgame.DTOs.SubmitTrafficAnswerResponse;
import com.pdsa.backend.trafficgame.DTOs.TrafficLeaderboardEntryResponse;
import com.pdsa.backend.trafficgame.DTOs.TrafficRoundDetailsResponse;
import com.pdsa.backend.trafficgame.service.TrafficGameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/traffic-game")
public class TrafficGameController {

    private final TrafficGameService trafficGameService;

    public TrafficGameController(TrafficGameService trafficGameService) {
        this.trafficGameService = trafficGameService;
    }

    @PostMapping("/rounds")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTrafficRoundResponse createRound(@Valid @RequestBody CreateTrafficRoundRequest request) {
        return trafficGameService.createRound(request);
    }

    @PostMapping("/answers")
    public SubmitTrafficAnswerResponse submitAnswer(@Valid @RequestBody SubmitTrafficAnswerRequest request) {
        return trafficGameService.submitAnswer(request);
    }

    @GetMapping("/rounds/{roundId}")
    public TrafficRoundDetailsResponse getRound(@PathVariable Long roundId) {
        return trafficGameService.getRoundDetails(roundId);
    }

    @GetMapping("/leaderboard")
    public List<TrafficLeaderboardEntryResponse> getLeaderboard(
            @RequestParam(defaultValue = "10") int limit) {
        return trafficGameService.getLeaderboard(limit);
    }
}
