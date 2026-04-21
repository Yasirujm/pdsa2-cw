package com.pdsa.backend.queens.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlgorithmRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "algorithm_type", nullable = false)
    private AlgorithmType algorithmType;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at", nullable = false)
    private LocalDateTime endedAt;

    @Column(name = "time_taken_ms", nullable = false)
    private Long timeTakenMs;

    @Column(name = "total_solutions_found", nullable = false)
    private Integer totalSolutionsFound;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum AlgorithmType {
        SEQUENTIAL, THREADED
    }
}