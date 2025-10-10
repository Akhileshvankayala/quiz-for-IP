package com.quiz.models;

/**
 * Question model representing a single quiz question
 * Contains question text, multiple choice options, correct answer, and fun fact
 */
public class Question {
    private int id;
    private String questionText;
    private String[] options;
    private int correctAnswerIndex;
    private String funFact;
    private String difficulty; // Easy, Medium, Hard
    private String hint; // Hint for the lifeline feature
    
    public Question() {
        this.options = new String[4]; // Always 4 options for consistency
    }
    
    public Question(int id, String questionText, String[] options, int correctAnswerIndex, String funFact, String difficulty) {
        this(id, questionText, options, correctAnswerIndex, funFact, difficulty, "Think carefully about this question.");
    }
    
    public Question(int id, String questionText, String[] options, int correctAnswerIndex, String funFact, String difficulty, String hint) {
        this.id = id;
        this.questionText = questionText;
        this.options = options != null ? options : new String[4];
        this.correctAnswerIndex = correctAnswerIndex;
        this.funFact = funFact;
        this.difficulty = difficulty;
        this.hint = hint;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getQuestionText() {
        return questionText;
    }
    
    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
    
    public String[] getOptions() {
        return options;
    }
    
    public void setOptions(String[] options) {
        this.options = options;
    }
    
    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
    
    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
    
    public String getCorrectAnswer() {
        if (options != null && correctAnswerIndex >= 0 && correctAnswerIndex < options.length) {
            return options[correctAnswerIndex];
        }
        return "";
    }
    
    public String getFunFact() {
        return funFact;
    }
    
    public void setFunFact(String funFact) {
        this.funFact = funFact;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getHint() {
        return hint;
    }
    
    public void setHint(String hint) {
        this.hint = hint;
    }
    
    /**
     * Check if the provided answer index is correct
     */
    public boolean isCorrectAnswer(int answerIndex) {
        return answerIndex == correctAnswerIndex;
    }
    
    /**
     * Get points for this question based on difficulty
     */
    public int getPoints() {
        switch (difficulty.toLowerCase()) {
            case "easy": return 10;
            case "medium": return 15;
            case "hard": return 20;
            default: return 10;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question{id=").append(id)
          .append(", questionText='").append(questionText).append('\'')
          .append(", difficulty='").append(difficulty).append('\'')
          .append(", options=[");
        
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(options[i]);
            }
        }
        
        sb.append("], correctAnswer=").append(getCorrectAnswer())
          .append(", funFact='").append(funFact).append('\'')
          .append('}');
        
        return sb.toString();
    }
}