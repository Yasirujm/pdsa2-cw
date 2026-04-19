package com.pdsa.queens.repository;

import com.pdsa.queens.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

    Optional<Solution> findByPlacementHash(String hash);

    long countByIsClaimedFalse();

    long countByIsClaimedTrue();

    @Modifying
    @Transactional
    @Query("UPDATE Solution s SET s.isClaimed = false, s.claimedBy = null, s.claimedAt = null")
    void resetAllClaims();
}