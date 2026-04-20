package com.pdsa.backend.minimumcost.service;

import com.pdsa.backend.minimumcost.dto.MinimumCostRoundResponse;
import com.pdsa.backend.minimumcost.dto.MinimumCostSubmitAnswerRequest;
import com.pdsa.backend.minimumcost.dto.MinimumCostSubmitAnswerResponse;
import com.pdsa.backend.minimumcost.entity.CostEntry;
import com.pdsa.backend.minimumcost.entity.MinimumCostRound;
import com.pdsa.backend.minimumcost.entity.MinimumCostPlayerAnswer;
import com.pdsa.backend.minimumcost.repository.MinimumCostRoundRepository;
import com.pdsa.backend.minimumcost.repository.MinimumCostPlayerAnswerRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class MinimumCostGameService {
    private final CostMatrixGeneratorService generatorService;
    private final HungarianSolverService hungarianSolverService;
    private final MinCostMaxFlowSolverService minCostMaxFlowSolverService;
    private final MinimumCostRoundRepository roundRepository;
    private final MinimumCostPlayerAnswerRepository minimumCostPlayerAnswerRepository;

    public MinimumCostGameService(CostMatrixGeneratorService generatorService, HungarianSolverService hungarianSolverService, MinCostMaxFlowSolverService minCostMaxFlowSolverService, MinimumCostRoundRepository roundRepository, MinimumCostPlayerAnswerRepository minimumCostPlayerAnswerRepository) {
        this.generatorService = generatorService;
        this.hungarianSolverService = hungarianSolverService;
        this.minCostMaxFlowSolverService = minCostMaxFlowSolverService;
        this.roundRepository = roundRepository;
        this.minimumCostPlayerAnswerRepository = minimumCostPlayerAnswerRepository;
    }

    public MinimumCostRoundResponse createRound(int taskCount, boolean useRandomSize) {
        Random random = new Random();

        if (useRandomSize) {
            taskCount = 50 + random.nextInt(51); // 50 to 100
        }

        int[][] matrix = generatorService.generateMatrix(taskCount);
        long hungarianStart = System.nanoTime();
        int hungarianAnswer = hungarianSolverService.solve(copyMatrix(matrix));
        long hungarianTime = System.nanoTime() - hungarianStart;

        long flowStart = System.nanoTime();
        int minCostFlowAnswer = minCostMaxFlowSolverService.solve(copyMatrix(matrix));
        long flowTime = System.nanoTime() - flowStart;

        int correctAnswer = hungarianAnswer;

        List<Integer> choices = buildChoices(correctAnswer);
        MinimumCostRound round = new MinimumCostRound();
        round.setTaskCount(taskCount);
        round.setHungarianMinCost(hungarianAnswer);
        round.setMinCostFlowMinCost(minCostFlowAnswer);
        round.setHungarianTimeNanos(hungarianTime);
        round.setMinCostFlowTimeNanos(flowTime);
        round.setCorrectAnswer(correctAnswer);

        for (int i = 0; i < taskCount; i++) {
            for (int j = 0; j < taskCount; j++) {
                round.addCost(new CostEntry(i + 1, j + 1, matrix[i][j]));
            }
        }

        MinimumCostRound saved = roundRepository.save(round);

        MinimumCostRoundResponse response = new MinimumCostRoundResponse();
        response.setGameRoundId(saved.getId());
        response.setTaskCount(taskCount);
        response.setCostMatrix(matrix);
        response.setAnswerChoices(choices);
        response.setHungarianAnswer(hungarianAnswer);
        response.setMinCostFlowAnswer(minCostFlowAnswer);
        response.setHungarianTimeNanos(hungarianTime);
        response.setMinCostFlowTimeNanos(flowTime);

        return response;
    }

    public MinimumCostSubmitAnswerResponse submitAnswer(MinimumCostSubmitAnswerRequest request) {
        MinimumCostRound round = roundRepository.findById(request.getGameRoundId()).orElseThrow(() -> new IllegalArgumentException("Game round not found"));
        boolean correct = round.getCorrectAnswer() == request.getSelectedAnswer();
        String result = correct ? "WIN" : "LOSE";
        MinimumCostPlayerAnswer answer = new MinimumCostPlayerAnswer();
        answer.setGameRound(round);
        answer.setPlayerName(request.getPlayerName().trim());
        answer.setSelectedAnswer(request.getSelectedAnswer());
        answer.setCorrectAnswer(round.getCorrectAnswer());
        answer.setCorrect(correct);
        minimumCostPlayerAnswerRepository.save(answer);

        return new MinimumCostSubmitAnswerResponse(correct, result, round.getCorrectAnswer());
    }

    private List<Integer> buildChoices(int correctAnswer) {
        Set<Integer> choices = new HashSet<>();
        Random random = new Random();
        choices.add(correctAnswer);

        while (choices.size() < 3) {
            int delta = 50 + random.nextInt(351);
            int option = random.nextBoolean() ? correctAnswer + delta :
                    correctAnswer - delta;
            if (option > 0) {
                choices.add(option);
            }
        }

        List<Integer> result = new ArrayList<>(choices);
        Collections.shuffle(result);
        return result;
    }

    private int[][] copyMatrix(int[][] original) {
        int[][] copy = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }
}

