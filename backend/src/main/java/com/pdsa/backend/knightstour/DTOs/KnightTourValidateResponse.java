package com.pdsa.backend.knightstour.DTOs;

public class KnightTourValidateResponse {

    private boolean correct;
    private String message;

    public KnightTourValidateResponse() {
    }

    public KnightTourValidateResponse(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}