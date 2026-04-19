package com.pdsa.backend.knightstour.DTOs;

public class KnightTourSaveResultRequest {

    private String playerName;
    private int boardSize;
    private int startRow;
    private int startCol;
    private boolean completed;
    private int moveCount;
    private long timeTakenMillis;
    private String validationMessage;

    public KnightTourSaveResultRequest() {
    }

    public KnightTourSaveResultRequest(
            String playerName,
            int boardSize,
            int startRow,
            int startCol,
            boolean completed,
            int moveCount,
            long timeTakenMillis,
            String validationMessage
    ) {
        this.playerName = playerName;
        this.boardSize = boardSize;
        this.startRow = startRow;
        this.startCol = startCol;
        this.completed = completed;
        this.moveCount = moveCount;
        this.timeTakenMillis = timeTakenMillis;
        this.validationMessage = validationMessage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public long getTimeTakenMillis() {
        return timeTakenMillis;
    }

    public void setTimeTakenMillis(long timeTakenMillis) {
        this.timeTakenMillis = timeTakenMillis;
    }

    public String getValidationMessage() {
        return validationMessage;
    }

    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }
}