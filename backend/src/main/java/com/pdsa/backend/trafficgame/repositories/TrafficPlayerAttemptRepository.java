package com.pdsa.backend.trafficgame.repositories;

import com.pdsa.backend.trafficgame.entity.TrafficPlayerAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficPlayerAttemptRepository extends JpaRepository<TrafficPlayerAttempt, Long> {

    Page<TrafficPlayerAttempt> findByOrderByCreatedAtDesc(Pageable pageable);
}
