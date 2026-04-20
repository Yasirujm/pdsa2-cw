package com.pdsa.backend.snakesladder.repositories;

import com.pdsa.backend.snakesladder.entity.GameRound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRoundRepository extends JpaRepository<GameRound, Long> {

}

