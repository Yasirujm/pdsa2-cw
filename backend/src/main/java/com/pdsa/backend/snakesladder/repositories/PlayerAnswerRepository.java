package com.pdsa.backend.snakesladder.repositories;

import com.pdsa.backend.snakesladder.entity.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, Long> {

}
