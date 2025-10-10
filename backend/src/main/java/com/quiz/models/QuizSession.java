package com.quiz.models;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * QuizSession model to track the current quiz session
 */
public class QuizSession {
    private String sessionId;
    private int currentQuestionIndex;
    private int totalQuestions;
    private int score;
    private int correctAnswers;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long totalTimeSpent; // in milliseconds
    private boolean isCompleted;
    private String playerName; // Optional for demo
    
    public QuizSession() {
        this.sessionId = generateSessionId();
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.correctAnswers = 0;
        this.startTime = LocalDateTime.now();
        this.isCompleted = false;
        this.totalTimeSpent = 0;
    }
    
    public QuizSession(String playerName) {
        this();
        this.playerName = playerName;
    }
    
    /**
     * Generate a simple session ID
     */
    private String generateSessionId() {
        return "QUIZ_" + System.currentTimeMillis();
    }
    
    /**
     * Move to next question
     */
    public void nextQuestion() {
        if (currentQuestionIndex < totalQuestions - 1) {
            currentQuestionIndex++;
        }
    }
    
    /**
     * Add score for correct answer
     */
    public void addScore(int points) {
        this.score += points;
        this.correctAnswers++;
    }
    
    /**
     * Complete the quiz session
     */
    public void completeQuiz() {
        this.endTime = LocalDateTime.now();
        this.isCompleted = true;
        if (startTime != null && endTime != null) {
            this.totalTimeSpent = Duration.between(startTime, endTime).toMillis();
        }
    }
    
    /**
     * Calculate accuracy percentage
     */
    public double getAccuracy() {
        if (totalQuestions == 0) return 0.0;
        return (double) correctAnswers / totalQuestions * 100;
    }
    
    /**
     * Get time bonus based on speed
     */
    public int getTimeBonus() {
        if (totalTimeSpent == 0) return 0;
        
        // Award bonus for completing under certain time thresholds
        long minutes = totalTimeSpent / (1000 * 60);
        if (minutes <= 5) return 50;
        else if (minutes <= 8) return 30;
        else if (minutes <= 12) return 10;
        else return 0;
    }
    
    /**
     * Get final score including time bonus
     */
    public int getFinalScore() {
        return score + getTimeBonus();
    }
    
    // Getters and Setters
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }
    
    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }
    
    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    
    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public long getTotalTimeSpent() {
        return totalTimeSpent;
    }
    
    public void setTotalTimeSpent(long totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }
    
    public boolean isCompleted() {
        return isCompleted;
    }
    
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    @Override
    public String toString() {
        return "QuizSession{" +
                "sessionId='" + sessionId + '\'' +
                ", currentQuestionIndex=" + currentQuestionIndex +
                ", totalQuestions=" + totalQuestions +
                ", score=" + score +
                ", correctAnswers=" + correctAnswers +
                ", accuracy=" + String.format("%.1f", getAccuracy()) + "%" +
                ", totalTimeSpent=" + totalTimeSpent + "ms" +
                ", isCompleted=" + isCompleted +
                ", playerName='" + playerName + '\'' +
                '}';
    }
}