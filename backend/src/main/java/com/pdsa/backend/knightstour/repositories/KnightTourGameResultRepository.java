package com.pdsa.backend.knightstour.repositories;

import com.pdsa.backend.knightstour.entity.KnightTourGameResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KnightTourGameResultRepository extends JpaRepository<KnightTourGameResult, Long> {

    List<KnightTourGameResult> findAllByOrderByCompletedDescTimeTakenMillisAscMoveCountDescCreatedAtDesc();
}