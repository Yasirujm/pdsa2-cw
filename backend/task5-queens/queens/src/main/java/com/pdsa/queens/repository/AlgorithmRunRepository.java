package com.pdsa.queens.repository;

import com.pdsa.queens.entity.AlgorithmRun;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AlgorithmRunRepository extends JpaRepository<AlgorithmRun, Long> {
    List<AlgorithmRun> findTop20ByOrderByCreatedAtDesc();
}