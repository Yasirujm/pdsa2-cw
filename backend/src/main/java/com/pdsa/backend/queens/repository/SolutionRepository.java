package com.pdsa.backend.queens.repository;

import com.pdsa.backend.queens.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface SolutionRepository extends JpaRepository<Solution, Long> {

    Optional<Solution> findByPlacementHash(String hash);

    long countByIsClaimedFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Solution s SET s.isClaimed = false, " +
            "s.claimedBy = null, s.claimedAt = null")
    void resetAllClaims();

    @Modifying
    @Transactional
    @Query(value = "INSERT IGNORE INTO solutions " +
            "(placement, placement_hash, is_claimed, created_at) " +
            "VALUES (:placement, :hash, false, NOW())",
            nativeQuery = true)
    void insertIgnore(@Param("placement") String placement,
                      @Param("hash") String hash);
}