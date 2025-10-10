package com.quiz.models;

import java.time.LocalDateTime;

/**
 * UserAnswer model to track user responses
 */
public class UserAnswer {
    private int questionId;
    private int selectedAnswerIndex;
    private boolean isCorrect;
    private long timeSpent; // in milliseconds
    private LocalDateTime answeredAt;
    
    public UserAnswer() {
        this.answeredAt = LocalDateTime.now();
    }
    
    public UserAnswer(int questionId, int selectedAnswerIndex, boolean isCorrect, long timeSpent) {
        this.questionId = questionId;
        this.selectedAnswerIndex = selectedAnswerIndex;
        this.isCorrect = isCorrect;
        this.timeSpent = timeSpent;
        this.answeredAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public int getSelectedAnswerIndex() {
        return selectedAnswerIndex;
    }
    
    public void setSelectedAnswerIndex(int selectedAnswerIndex) {
        this.selectedAnswerIndex = selectedAnswerIndex;
    }
    
    public boolean isCorrect() {
        return isCorrect;
    }
    
    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
    
    public long getTimeSpent() {
        return timeSpent;
    }
    
    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }
    
    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }
    
    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }
    
    @Override
    public String toString() {
        return "UserAnswer{" +
                "questionId=" + questionId +
                ", selectedAnswerIndex=" + selectedAnswerIndex +
                ", isCorrect=" + isCorrect +
                ", timeSpent=" + timeSpent + "ms" +
                ", answeredAt=" + answeredAt +
                '}';
    }
}