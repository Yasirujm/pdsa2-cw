package com.pdsa.backend.queens.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdsa.backend.queens.algorithm.ThreadedQueensSolver;
import com.pdsa.backend.queens.algorithm.SequentialQueensSolver;
import com.pdsa.backend.queens.entity.AlgorithmRun;
import com.pdsa.backend.queens.entity.GameSession;
import com.pdsa.backend.queens.entity.Solution;
import com.pdsa.backend.queens.repository.AlgorithmRunRepository;
import com.pdsa.backend.queens.repository.GameSessionRepository;
import com.pdsa.backend.queens.repository.SolutionRepository;
import com.pdsa.backend.queens.entity.*;
import com.pdsa.backend.queens.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueensGameService {

    private final SequentialQueensSolver sequentialSolver;
    private final ThreadedQueensSolver threadedSolver;
    private final SolutionRepository solutionRepository;
    private final AlgorithmRunRepository algorithmRunRepository;
    private final GameSessionRepository gameSessionRepository;
    private final ObjectMapper objectMapper;

    /**
     * Run on application startup: compute all solutions if not already in DB.
     * This ensures the DB is populated before any player submits an answer.
     */
    /*@EventListener(ApplicationReadyEvent.class)
    public void initializeSolutions() {
        long count = solutionRepository.count();
        if (count > 0) {
            log.info("Solutions already in DB ({}). Skipping pre-computation.", count);
            return;
        }
        log.info("Starting pre-computation of all solutions...");
        runSequentialAndStore();
        runThreadedAndStore(); // Records timing only; solutions already stored
    }*/

    @EventListener(ApplicationReadyEvent.class)
    public void initializeSolutions() {
        log.info("Running algorithms for timing comparison...");
        runSequentialAndRecord();
        runThreadedAndRecord();
        log.info("Algorithms complete. Game is ready.");
    }


    /*public void runSequentialAndStore() {
        log.info("Running SEQUENTIAL algorithm...");
        LocalDateTime start = LocalDateTime.now();
        long startMs = System.currentTimeMillis();

        List<int[]> solutions = sequentialSolver.findAllSolutions();

        long endMs = System.currentTimeMillis();
        LocalDateTime end = LocalDateTime.now();

        // Save solutions in batches
        saveSolutionsBatch(solutions);

        // Record timing
        AlgorithmRun run = AlgorithmRun.builder()
                .algorithmType(AlgorithmRun.AlgorithmType.SEQUENTIAL)
                .startedAt(start)
                .endedAt(end)
                .timeTakenMs(endMs - startMs)
                .totalSolutionsFound(solutions.size())
                .build();
        algorithmRunRepository.save(run);

        log.info("SEQUENTIAL complete: {} solutions in {}ms", solutions.size(), endMs - startMs);
    }*/

    public void runSequentialAndRecord() {
        try {
            log.info("Running SEQUENTIAL algorithm...");
            LocalDateTime start = LocalDateTime.now();
            long startMs = System.currentTimeMillis();

            List<int[]> solutions = sequentialSolver.findAllSolutions();

            long endMs = System.currentTimeMillis();
            LocalDateTime end = LocalDateTime.now();

            // Record timing only — do NOT save solutions to DB
            algorithmRunRepository.save(AlgorithmRun.builder()
                    .algorithmType(AlgorithmRun.AlgorithmType.SEQUENTIAL)
                    .startedAt(start)
                    .endedAt(end)
                    .timeTakenMs(endMs - startMs)
                    .totalSolutionsFound(solutions.size())
                    .build());

            log.info("SEQUENTIAL complete: {} solutions in {}ms",
                    solutions.size(), endMs - startMs);

        } catch (Exception e) {
            log.error("Sequential algorithm failed", e);
        }
    }

    /*@Transactional
    public void runThreadedAndStore() {
        try {
            log.info("Running THREADED algorithm...");
            LocalDateTime start = LocalDateTime.now();
            long startMs = System.currentTimeMillis();

            List<int[]> solutions = threadedSolver.findAllSolutions();

            long endMs = System.currentTimeMillis();
            LocalDateTime end = LocalDateTime.now();

            AlgorithmRun run = AlgorithmRun.builder()
                    .algorithmType(AlgorithmRun.AlgorithmType.THREADED)
                    .startedAt(start)
                    .endedAt(end)
                    .timeTakenMs(endMs - startMs)
                    .totalSolutionsFound(solutions.size())
                    .build();
            algorithmRunRepository.save(run);

            log.info("THREADED complete: {} solutions in {}ms", solutions.size(), endMs - startMs);
        } catch (Exception e) {
            log.error("Threaded algorithm failed", e);
            // Do not rethrow — log and continue
        }
    }*/
    public void runThreadedAndRecord() {
        try {
            log.info("Running THREADED algorithm...");
            LocalDateTime start = LocalDateTime.now();
            long startMs = System.currentTimeMillis();

            List<int[]> solutions = threadedSolver.findAllSolutions();

            long endMs = System.currentTimeMillis();
            LocalDateTime end = LocalDateTime.now();

            // Record timing only — do NOT save solutions to DB
            algorithmRunRepository.save(AlgorithmRun.builder()
                    .algorithmType(AlgorithmRun.AlgorithmType.THREADED)
                    .startedAt(start)
                    .endedAt(end)
                    .timeTakenMs(endMs - startMs)
                    .totalSolutionsFound(solutions.size())
                    .build());

            log.info("THREADED complete: {} solutions in {}ms",
                    solutions.size(), endMs - startMs);

        } catch (Exception e) {
            log.error("Threaded algorithm failed", e);
        }
    }

    /**
     * Process a player's submitted queen placement.
     * Returns WIN, LOSE, or DRAW.
     */
    /*@Transactional
    public GameResult submitAnswer(String playerName, int[] placement) {
        if (playerName == null || playerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        if (placement == null || placement.length != 8) {
            throw new IllegalArgumentException("Must place exactly 8 queens");
        }
        for (int val : placement) {
            if (val < 0 || val > 255) {
                throw new IllegalArgumentException("Each placement value must be between 0 and 15");
            }
        }

        // Check if placement is a valid solution
        String hash = computeHash(placement);
        Optional<Solution> found = solutionRepository.findByPlacementHash(hash);

        GameSession session;

        if (found.isEmpty()) {
            // LOSE — not a valid solution
            session = GameSession.builder()
                    .playerName(playerName.trim())
                    .submittedPlacement(arrayToJson(placement))
                    .isCorrect(false)
                    .result(GameSession.GameResult.LOSE)
                    .build();
            gameSessionRepository.save(session);
            return new GameResult("LOSE", "Invalid placement. Not a valid solution.");
        }

        Solution solution = found.get();

        if (solution.getIsClaimed()) {
            // DRAW — already claimed by someone else
            session = GameSession.builder()
                    .playerName(playerName.trim())
                    .submittedPlacement(arrayToJson(placement))
                    .isCorrect(true)
                    .result(GameSession.GameResult.DRAW)
                    .solution(solution)
                    .build();
            gameSessionRepository.save(session);
            return new GameResult("DRAW",
                    "Valid solution! But already found by " + solution.getClaimedBy() + ". Try a different arrangement.");
        }

        // WIN — valid and unclaimed
        solution.setIsClaimed(true);
        solution.setClaimedBy(playerName.trim());
        solution.setClaimedAt(LocalDateTime.now());
        solutionRepository.save(solution);

        session = GameSession.builder()
                .playerName(playerName.trim())
                .submittedPlacement(arrayToJson(placement))
                .isCorrect(true)
                .result(GameSession.GameResult.WIN)
                .solution(solution)
                .build();
        gameSessionRepository.save(session);

        // Check if all solutions have been claimed — reset if so
        long unclaimed = solutionRepository.countByIsClaimedFalse();
        if (unclaimed == 0) {
            solutionRepository.resetAllClaims();
            log.info("All solutions claimed! Resetting flags.");
        }

        return new GameResult("WIN", "Correct! You found a unique solution!");
    }*/
    @Transactional
    public GameResult submitAnswer(String playerName, int[] placement) {

        // VALIDATION
        if (playerName == null || playerName.trim().isEmpty())
            throw new IllegalArgumentException("Player name cannot be empty");
        if (placement == null || placement.length != 8)
            throw new IllegalArgumentException("Must place exactly 8 queens");
        for (int val : placement)
            if (val < 0 || val > 255)
                throw new IllegalArgumentException("Values must be 0-255");

        // STEP 1: Validate mathematically — is this actually a valid solution?
        if (!isValidSolution(placement)) {
            gameSessionRepository.save(GameSession.builder()
                    .playerName(playerName.trim())
                    .submittedPlacement(arrayToJson(placement))
                    .isCorrect(false)
                    .result(GameSession.GameResult.LOSE)
                    .build());
            return new GameResult("LOSE",
                    "Invalid placement. Not a valid solution.");
        }

        // STEP 2: Valid solution — check if already claimed
        String hash = computeHash(placement);
        Optional<Solution> found = solutionRepository.findByPlacementHash(hash);

        if (found.isPresent() && found.get().getIsClaimed()) {
            // DRAW — valid but already claimed
            gameSessionRepository.save(GameSession.builder()
                    .playerName(playerName.trim())
                    .submittedPlacement(arrayToJson(placement))
                    .isCorrect(true)
                    .result(GameSession.GameResult.DRAW)
                    .solution(found.get())
                    .build());
            return new GameResult("DRAW",
                    "Valid solution! But already found by "
                            + found.get().getClaimedBy()
                            + ". Try a different arrangement.");
        }

        // STEP 3: WIN — valid and unclaimed
        // Save to solutions table if not already there
        Solution solution;
        if (found.isEmpty()) {
            solution = Solution.builder()
                    .placement(arrayToJson(placement))
                    .placementHash(hash)
                    .isClaimed(true)
                    .claimedBy(playerName.trim())
                    .claimedAt(LocalDateTime.now())
                    .build();
            solutionRepository.save(solution);
        } else {
            solution = found.get();
            solution.setIsClaimed(true);
            solution.setClaimedBy(playerName.trim());
            solution.setClaimedAt(LocalDateTime.now());
            solutionRepository.save(solution);
        }

        gameSessionRepository.save(GameSession.builder()
                .playerName(playerName.trim())
                .submittedPlacement(arrayToJson(placement))
                .isCorrect(true)
                .result(GameSession.GameResult.WIN)
                .solution(solution)
                .build());

        // Reset all claims if all stored solutions are claimed
        long unclaimed = solutionRepository.countByIsClaimedFalse();
        if (unclaimed == 0) {
            solutionRepository.resetAllClaims();
            log.info("All solutions claimed. Resetting flags.");
        }

        return new GameResult("WIN", "Correct! You found a unique solution!");
    }

    // Mathematical validation — checks all three constraints
    private boolean isValidSolution(int[] placement) {
        if (placement.length != 8) return false;

        int[] sorted = placement.clone();
        java.util.Arrays.sort(sorted);

        for (int i = 0; i < sorted.length; i++) {
            int col1 = sorted[i] / 16;
            int row1 = sorted[i] % 16;

            for (int j = i + 1; j < sorted.length; j++) {
                int col2 = sorted[j] / 16;
                int row2 = sorted[j] % 16;

                // Same row
                if (row1 == row2) return false;

                // Same column
                if (col1 == col2) return false;

                // Same diagonal
                if (Math.abs(row1 - row2) == Math.abs(col1 - col2))
                    return false;
            }
        }
        return true;
    }

    public List<GameSession> getLeaderboard() {
        return gameSessionRepository.findByIsCorrectTrueOrderByPlayedAtDesc();
    }

    public List<AlgorithmRun> getAlgorithmStats() {
        return algorithmRunRepository.findTop20ByOrderByCreatedAtDesc();
    }

    // --- Private helper methods ---

    /*private void saveSolutionsBatch(List<int[]> solutions) {
        log.info("Saving {} solutions to database...", solutions.size());
        int saved = 0;
        for (int[] sol : solutions) {
            try {
                String json = arrayToJson(sol);
                String hash = computeHash(sol);
                solutionRepository.insertIgnore(json, hash);
                saved++;
                if (saved % 50000 == 0) {
                    log.info("Saved {}/{} solutions...", saved, solutions.size());
                }
            } catch (Exception e) {
                log.warn("Failed to save one solution: {}", e.getMessage());
            }
        }
        log.info("Finished saving. Total saved: {}", saved);
    }*/

    private String arrayToJson(int[] arr) {
        try {
            return objectMapper.writeValueAsString(arr);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize placement", e);
        }
    }

    private String computeHash(int[] placement) {
        try {
            String json = arrayToJson(placement);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(json.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash computation failed", e);
        }
    }

    // Inner result class
    public record GameResult(String result, String message) {}
}