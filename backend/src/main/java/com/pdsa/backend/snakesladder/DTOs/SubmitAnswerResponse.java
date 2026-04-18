package com.pdsa.backend.snakesladder.DTOs;

public class SubmitAnswerResponse {
    private boolean correct;
    private String result;
    private int correctAnswer;

    public SubmitAnswerResponse(boolean correct, String result, int
            correctAnswer) {
        this.correct = correct;
        this.result = result;
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() { return correct; }

    public String getResult() { return result; }

    public int getCorrectAnswer() { return correctAnswer; }
}
