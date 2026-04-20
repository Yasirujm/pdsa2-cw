package com.pdsa.backend.queens.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_name", nullable = false)
    private String playerName;

    @Column(name = "submitted_placement", columnDefinition = "JSON", nullable = false)
    private String submittedPlacement;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameResult result;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Solution solution;

    @Column(name = "played_at")
    private LocalDateTime playedAt = LocalDateTime.now();

    public enum GameResult {
        WIN, LOSE, DRAW
    }
}