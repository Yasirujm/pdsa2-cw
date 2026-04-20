package com.pdsa.queens.repository;

import com.pdsa.queens.entity.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByIsCorrectTrueOrderByPlayedAtDesc();
}