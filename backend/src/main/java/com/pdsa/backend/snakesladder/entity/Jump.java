package com.pdsa.backend.snakesladder.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "board_jumps")
public class Jump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int startCell;
    private int endCell;
    private String jumpType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_round_id", nullable = false)
    private GameRound gameRound;

    public Jump() {}

    public Jump(int startCell, int endCell, String jumpType) {
        this.startCell = startCell;
        this.endCell = endCell;
        this.jumpType = jumpType;
    }

    public Long getId() { return id; }

    public int getStartCell() { return startCell; }

    public void setStartCell(int startCell) { this.startCell = startCell; }

    public int getEndCell() { return endCell; }

    public void setEndCell(int endCell) { this.endCell = endCell; }

    public String getJumpType() { return jumpType; }

    public void setJumpType(String jumpType) { this.jumpType = jumpType; }

    public GameRound getGameRound() { return gameRound; }

    public void setGameRound(GameRound gameRound) {
        this.gameRound = gameRound;
    }
}