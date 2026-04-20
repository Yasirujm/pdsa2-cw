package com.pdsa.backend.snakesladder.service;

import com.pdsa.backend.snakesladder.DTOs.RoundResponse;
import com.pdsa.backend.snakesladder.DTOs.SubmitAnswerRequest;
import com.pdsa.backend.snakesladder.DTOs.SubmitAnswerResponse;
import com.pdsa.backend.snakesladder.entity.GameRound;
import com.pdsa.backend.snakesladder.entity.Jump;
import com.pdsa.backend.snakesladder.entity.PlayerAnswer;
import com.pdsa.backend.snakesladder.repositories.GameRoundRepository;
import com.pdsa.backend.snakesladder.repositories.PlayerAnswerRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GameService {
    private final BoardGeneratorService boardGeneratorService;
    private final SnakeLadderSolverService solverService;
    private final GameRoundRepository gameRoundRepository;
    private final PlayerAnswerRepository playerAnswerRepository;

    public GameService(BoardGeneratorService boardGeneratorService, SnakeLadderSolverService solverService, GameRoundRepository gameRoundRepository, PlayerAnswerRepository playerAnswerRepository) {
        this.boardGeneratorService = boardGeneratorService;
        this.solverService = solverService;
        this.gameRoundRepository = gameRoundRepository;
        this.playerAnswerRepository = playerAnswerRepository;
    }

    public RoundResponse createRound(int boardSize) {
        Map<Integer, Integer> jumps = boardGeneratorService.generateJumps(boardSize);
        long bfsStart = System.nanoTime();
        int bfsAnswer = solverService.minThrowsBfs(boardSize, jumps);
        long bfsTime = System.nanoTime() - bfsStart;
        long dijkstraStart = System.nanoTime();
        int dijkstraAnswer = solverService.minThrowsDijkstra(boardSize, jumps);
        long dijkstraTime = System.nanoTime() - dijkstraStart;
        int correctAnswer = bfsAnswer;
        List<Integer> choices = buildChoices(correctAnswer);
        GameRound round = new GameRound();

        round.setBoardSize(boardSize);
        round.setTotalCells(boardSize * boardSize);
        round.setBfsMinThrows(bfsAnswer);
        round.setDijkstraMinThrows(dijkstraAnswer);
        round.setBfsTimeNanos(bfsTime);
        round.setDijkstraTimeNanos(dijkstraTime);
        round.setCorrectAnswer(correctAnswer);

        for (Map.Entry<Integer, Integer> entry : jumps.entrySet()) {
            String type = entry.getKey() < entry.getValue() ? "LADDER" :
                    "SNAKE";
            round.addJump(new Jump(entry.getKey(), entry.getValue(), type));
        }

        GameRound saved = gameRoundRepository.save(round);
        RoundResponse response = new RoundResponse();
        response.setGameRoundId(saved.getId());
        response.setBoardSize(boardSize);
        response.setTotalCells(boardSize * boardSize);
        response.setJumps(jumps);
        response.setAnswerChoices(choices);
        response.setBfsAnswer(bfsAnswer);
        response.setDijkstraAnswer(dijkstraAnswer);
        response.setBfsTimeNanos(bfsTime);
        response.setDijkstraTimeNanos(dijkstraTime);
        return response;
    }
    public SubmitAnswerResponse submitAnswer(SubmitAnswerRequest request) {
        GameRound round = gameRoundRepository.findById(request.getGameRoundId()).orElseThrow(() -> new IllegalArgumentException("Game round not found"));
        boolean correct = round.getCorrectAnswer() == request.getSelectedAnswer();

        String result = correct ? "WIN" : "LOSE";
        PlayerAnswer answer = new PlayerAnswer();
        answer.setGameRound(round);
        answer.setPlayerName(request.getPlayerName().trim());
        answer.setSelectedAnswer(request.getSelectedAnswer());
        answer.setCorrectAnswer(round.getCorrectAnswer());
        answer.setCorrect(correct);
        playerAnswerRepository.save(answer);
        return new SubmitAnswerResponse(correct, result,
                round.getCorrectAnswer());
    }
    private List<Integer> buildChoices(int correctAnswer) {
        Set<Integer> choices = new HashSet<>();
        Random random = new Random();
        choices.add(correctAnswer);
        while (choices.size() < 3) {
            int delta = random.nextInt(4) + 1;
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
}

