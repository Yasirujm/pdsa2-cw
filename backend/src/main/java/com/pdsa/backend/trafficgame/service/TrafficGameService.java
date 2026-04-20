package com.pdsa.backend.trafficgame.service;

import com.pdsa.backend.trafficgame.DTOs.CreateTrafficRoundRequest;
import com.pdsa.backend.trafficgame.DTOs.CreateTrafficRoundResponse;
import com.pdsa.backend.trafficgame.DTOs.SubmitTrafficAnswerRequest;
import com.pdsa.backend.trafficgame.DTOs.SubmitTrafficAnswerResponse;
import com.pdsa.backend.trafficgame.DTOs.TrafficEdgeResponse;
import com.pdsa.backend.trafficgame.DTOs.TrafficLeaderboardEntryResponse;
import com.pdsa.backend.trafficgame.DTOs.TrafficRoundDetailsResponse;
import com.pdsa.backend.trafficgame.config.ResourceNotFoundException;
import com.pdsa.backend.trafficgame.entity.TrafficEdge;
import com.pdsa.backend.trafficgame.entity.TrafficPlayerAttempt;
import com.pdsa.backend.trafficgame.entity.TrafficRound;
import com.pdsa.backend.trafficgame.repositories.TrafficPlayerAttemptRepository;
import com.pdsa.backend.trafficgame.repositories.TrafficRoundRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TrafficGameService {

    private final TrafficRoundRepository trafficRoundRepository;
    private final TrafficPlayerAttemptRepository trafficPlayerAttemptRepository;
    private final TrafficFlowAlgorithmService trafficFlowAlgorithmService;

    public TrafficGameService(TrafficRoundRepository trafficRoundRepository,
                              TrafficPlayerAttemptRepository trafficPlayerAttemptRepository,
                              TrafficFlowAlgorithmService trafficFlowAlgorithmService) {
        this.trafficRoundRepository = trafficRoundRepository;
        this.trafficPlayerAttemptRepository = trafficPlayerAttemptRepository;
        this.trafficFlowAlgorithmService = trafficFlowAlgorithmService;
    }

    @Transactional
    public CreateTrafficRoundResponse createRound(CreateTrafficRoundRequest request) {
        if (request.getMinCapacity() > request.getMaxCapacity()) {
            throw new IllegalArgumentException("minCapacity cannot be greater than maxCapacity");
        }

        String source = trafficFlowAlgorithmService.getSourceNode();
        String sink = trafficFlowAlgorithmService.getSinkNode();
        List<TrafficEdgeResponse> generatedEdges = trafficFlowAlgorithmService.generateRandomEdges(
                request.getMinCapacity(), request.getMaxCapacity());

        TrafficFlowAlgorithmService.MaxFlowResult ekResult =
                trafficFlowAlgorithmService.solveWithEdmondsKarp(generatedEdges, source, sink);
        TrafficFlowAlgorithmService.MaxFlowResult ffResult =
                trafficFlowAlgorithmService.solveWithFordFulkerson(generatedEdges, source, sink);

        TrafficRound round = new TrafficRound();
        round.setSourceNode(source);
        round.setSinkNode(sink);
        round.setMaxFlowEdmondsKarp(ekResult.getMaxFlow());
        round.setMaxFlowFordFulkerson(ffResult.getMaxFlow());
        round.setEdmondsKarpTimeNanos(ekResult.getTimeNanos());
        round.setFordFulkersonTimeNanos(ffResult.getTimeNanos());

        List<TrafficEdgeResponse> responseEdges = new ArrayList<>();
        for (TrafficEdgeResponse edge : generatedEdges) {
            int flow = ekResult.getEdgeFlows().getOrDefault(edge.getFrom() + "-" + edge.getTo(), 0);
            round.addEdge(new TrafficEdge(edge.getFrom(), edge.getTo(), edge.getCapacity(), flow));
            responseEdges.add(new TrafficEdgeResponse(edge.getFrom(), edge.getTo(), edge.getCapacity(), flow));
        }

        TrafficRound savedRound = trafficRoundRepository.save(round);

        CreateTrafficRoundResponse response = new CreateTrafficRoundResponse();
        response.setRoundId(savedRound.getId());
        response.setNodes(trafficFlowAlgorithmService.getNodes());
        response.setSource(source);
        response.setSink(sink);
        response.setEdges(responseEdges);
        response.setMaxFlowEdmondsKarp(savedRound.getMaxFlowEdmondsKarp());
        response.setMaxFlowFordFulkerson(savedRound.getMaxFlowFordFulkerson());
        response.setEdmondsKarpTimeNanos(savedRound.getEdmondsKarpTimeNanos());
        response.setFordFulkersonTimeNanos(savedRound.getFordFulkersonTimeNanos());
        return response;
    }

    @Transactional
    public SubmitTrafficAnswerResponse submitAnswer(SubmitTrafficAnswerRequest request) {
        TrafficRound round = trafficRoundRepository.findById(request.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Round not found for id " + request.getRoundId()));

        boolean correct = request.getEstimatedMaxFlow() == round.getMaxFlowEdmondsKarp();

        TrafficPlayerAttempt attempt = new TrafficPlayerAttempt();
        attempt.setPlayerName(request.getPlayerName().trim());
        attempt.setSubmittedFlow(request.getEstimatedMaxFlow());
        attempt.setActualMaxFlow(round.getMaxFlowEdmondsKarp());
        attempt.setCorrect(correct);
        round.addAttempt(attempt);

        TrafficPlayerAttempt savedAttempt = trafficPlayerAttemptRepository.save(attempt);

        SubmitTrafficAnswerResponse response = new SubmitTrafficAnswerResponse();
        response.setAttemptId(savedAttempt.getId());
        response.setCorrect(correct);
        response.setSubmittedFlow(savedAttempt.getSubmittedFlow());
        response.setActualMaxFlow(savedAttempt.getActualMaxFlow());
        response.setEdmondsKarpTimeNanos(round.getEdmondsKarpTimeNanos());
        response.setFordFulkersonTimeNanos(round.getFordFulkersonTimeNanos());
        response.setEdgeFlows(mapEdges(round.getEdges()));
        return response;
    }

    @Transactional(readOnly = true)
    public TrafficRoundDetailsResponse getRoundDetails(Long roundId) {
        TrafficRound round = trafficRoundRepository.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Round not found for id " + roundId));

        TrafficRoundDetailsResponse response = new TrafficRoundDetailsResponse();
        response.setRoundId(round.getId());
        response.setSource(round.getSourceNode());
        response.setSink(round.getSinkNode());
        response.setNodes(trafficFlowAlgorithmService.getNodes());
        response.setEdges(mapEdges(round.getEdges()));
        response.setMaxFlowEdmondsKarp(round.getMaxFlowEdmondsKarp());
        response.setMaxFlowFordFulkerson(round.getMaxFlowFordFulkerson());
        response.setEdmondsKarpTimeNanos(round.getEdmondsKarpTimeNanos());
        response.setFordFulkersonTimeNanos(round.getFordFulkersonTimeNanos());
        response.setCreatedAt(round.getCreatedAt());
        return response;
    }

    @Transactional(readOnly = true)
    public List<TrafficLeaderboardEntryResponse> getLeaderboard(int limit) {
        int sanitizedLimit = Math.max(1, Math.min(limit, 100));
        return trafficPlayerAttemptRepository.findByOrderByCreatedAtDesc(PageRequest.of(0, sanitizedLimit))
                .map(attempt -> {
                    TrafficLeaderboardEntryResponse item = new TrafficLeaderboardEntryResponse();
                    item.setPlayerName(attempt.getPlayerName());
                    item.setRoundId(attempt.getTrafficRound().getId());
                    item.setSubmittedFlow(attempt.getSubmittedFlow());
                    item.setActualMaxFlow(attempt.getActualMaxFlow());
                    item.setCorrect(attempt.isCorrect());
                    item.setPlayedAt(attempt.getCreatedAt());
                    return item;
                })
                .getContent();
    }

    private List<TrafficEdgeResponse> mapEdges(List<TrafficEdge> edges) {
        List<TrafficEdgeResponse> mapped = new ArrayList<>();
        for (TrafficEdge edge : edges) {
            mapped.add(new TrafficEdgeResponse(edge.getFromNode(), edge.getToNode(), edge.getCapacity(), edge.getFlow()));
        }
        return mapped;
    }
}
