package com.pdsa.backend.snakesladder.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game_rounds")
public class GameRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int boardSize;
    private int totalCells;
    private int bfsMinThrows;
    private int dijkstraMinThrows;
    private long bfsTimeNanos;
    private long dijkstraTimeNanos;
    private int correctAnswer;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "gameRound", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Jump> jumps = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    public void addJump(Jump jump) {
        jumps.add(jump);
        jump.setGameRound(this);
    }
    public Long getId() { return id; }

    public int getBoardSize() { return boardSize; }

    public void setBoardSize(int boardSize) { this.boardSize = boardSize; }

    public int getTotalCells() { return totalCells; }

    public void setTotalCells(int totalCells) { this.totalCells = totalCells; }

    public int getBfsMinThrows() { return bfsMinThrows; }

    public void setBfsMinThrows(int bfsMinThrows) {
        this.bfsMinThrows = bfsMinThrows;
    }

    public int getDijkstraMinThrows() { return dijkstraMinThrows; }

    public void setDijkstraMinThrows(int dijkstraMinThrows) {
        this.dijkstraMinThrows = dijkstraMinThrows;
    }

    public long getBfsTimeNanos() { return bfsTimeNanos; }

    public void setBfsTimeNanos(long bfsTimeNanos) {
        this.bfsTimeNanos = bfsTimeNanos;
    }

    public long getDijkstraTimeNanos() { return dijkstraTimeNanos; }

    public void setDijkstraTimeNanos(long dijkstraTimeNanos) {
        this.dijkstraTimeNanos = dijkstraTimeNanos;
    }

    public int getCorrectAnswer() { return correctAnswer; }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Jump> getJumps() { return jumps; }
}
