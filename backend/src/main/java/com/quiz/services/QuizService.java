package com.quiz.services;

import com.quiz.datastructures.QuizLinkedList;
import com.quiz.datastructures.QuizStack;
import com.quiz.models.Question;
import com.quiz.models.QuizSession;
import com.quiz.models.UserAnswer;

/**
 * QuizService handles all quiz logic using custom data structures
 * Uses QuizLinkedList (Priority 2) for questions and QuizStack (Priority 1) for user navigation
 */
public class QuizService {
    private QuizLinkedList<Question> questions;
    private QuizStack<UserAnswer> userAnswers;
    private QuizSession currentSession;
    
    public QuizService() {
        this.questions = new QuizLinkedList<>();
        this.userAnswers = new QuizStack<>();
        initializeQuestions();
    }
    
    /**
     * Initialize Java-related quiz questions
     */
    private void initializeQuestions() {
        // Question 1 - Easy
        questions.add(new Question(1, 
            "What is the correct syntax for the main method in Java?",
            new String[]{
                "public static void main(String args[])",
                "static public void main(String[] args)",
                "public static void main(String[] args)",
                "public void main(String[] args)"
            },
            2, // Correct answer index
            "Fun Fact: The main method is the entry point of any Java application. The JVM calls this method when you run a Java program!",
            "Easy"
        ));
        
        // Question 2 - Easy
        questions.add(new Question(2,
            "Which of the following is NOT a Java primitive data type?",
            new String[]{
                "int",
                "String",
                "boolean",
                "char"
            },
            1, // Correct answer index
            "Fun Fact: String is actually a class in Java, not a primitive type. It's stored in the heap memory and has many useful methods!",
            "Easy"
        ));
        
        // Question 3 - Medium
        questions.add(new Question(3,
            "What will be the output of: System.out.println(10 + 20 + \"Hello\" + 30 + 40);",
            new String[]{
                "30Hello3040",
                "10203040Hello",
                "30Hello70",
                "1020Hello3040"
            },
            0, // Correct answer index
            "Fun Fact: In Java, string concatenation is evaluated left to right. Numbers are added until a string is encountered, then everything becomes string concatenation!",
            "Medium"
        ));
        
        // Question 4 - Medium
        questions.add(new Question(4,
            "Which keyword is used to prevent inheritance in Java?",
            new String[]{
                "static",
                "final",
                "private",
                "abstract"
            },
            1, // Correct answer index
            "Fun Fact: The 'final' keyword can be used with classes, methods, and variables. Final classes like String cannot be extended!",
            "Medium"
        ));
        
        // Question 5 - Hard
        questions.add(new Question(5,
            "What is the time complexity of adding an element to a HashMap in Java?",
            new String[]{
                "O(1) average case",
                "O(log n)",
                "O(n)",
                "O(n^2)"
            },
            0, // Correct answer index
            "Fun Fact: HashMap uses hash tables internally. In the best case, it offers O(1) insertion, but in worst case (when all keys hash to same bucket), it can degrade to O(n)!",
            "Hard"
        ));
        
        // Question 6 - Easy
        questions.add(new Question(6,
            "Which method is used to compare two strings in Java?",
            new String[]{
                "compare()",
                "equals()",
                "==",
                "compareTo()"
            },
            1, // Correct answer index
            "Fun Fact: Always use equals() for string comparison! The == operator compares references, not actual string content.",
            "Easy"
        ));
        
        // Question 7 - Medium
        questions.add(new Question(7,
            "What is the default value of a boolean variable in Java?",
            new String[]{
                "true",
                "false",
                "0",
                "null"
            },
            1, // Correct answer index
            "Fun Fact: All boolean instance variables are automatically initialized to false. Local boolean variables must be explicitly initialized!",
            "Medium"
        ));
        
        // Question 8 - Hard
        questions.add(new Question(8,
            "Which design pattern is implemented by the String class in Java?",
            new String[]{
                "Singleton",
                "Factory",
                "Immutable Object",
                "Observer"
            },
            2, // Correct answer index
            "Fun Fact: String objects are immutable in Java. Once created, they cannot be changed. This makes them thread-safe and allows for string pooling!",
            "Hard"
        ));
        
        // Question 9 - Medium
        questions.add(new Question(9,
            "What is the correct way to create a thread in Java?",
            new String[]{
                "Extend Thread class only",
                "Implement Runnable interface only",
                "Both extending Thread and implementing Runnable",
                "Use ThreadGroup class"
            },
            2, // Correct answer index
            "Fun Fact: Implementing Runnable is generally preferred over extending Thread because Java supports single inheritance, and you might want to extend another class!",
            "Medium"
        ));
        
        // Question 10 - Hard
        questions.add(new Question(10,
            "What happens when you call System.gc() in Java?",
            new String[]{
                "Forces immediate garbage collection",
                "Suggests JVM to run garbage collection",
                "Throws an exception",
                "Clears all static variables"
            },
            1, // Correct answer index
            "Fun Fact: System.gc() is just a suggestion to the JVM. The JVM may choose to ignore it! Modern JVMs are very efficient at managing memory automatically.",
            "Hard"
        ));
    }
    
    /**
     * Start a new quiz session
     */
    public QuizSession startQuiz(String playerName) {
        currentSession = new QuizSession(playerName);
        currentSession.setTotalQuestions(questions.size());
        userAnswers.clear(); // Clear previous answers using our custom stack
        return currentSession;
    }
    
    /**
     * Get current question using LinkedList
     */
    public Question getCurrentQuestion() {
        System.out.println("=== getCurrentQuestion called ===");
        System.out.println("currentSession: " + (currentSession != null ? "exists" : "null"));
        
        if (currentSession == null) {
            System.out.println("Returning null: currentSession is null");
            return null;
        }
        
        int currentIndex = currentSession.getCurrentQuestionIndex();
        int totalQuestions = questions.size();
        System.out.println("Current question index: " + currentIndex);
        System.out.println("Total questions available: " + totalQuestions);
        
        if (currentIndex >= totalQuestions) {
            System.out.println("Returning null: currentIndex (" + currentIndex + ") >= totalQuestions (" + totalQuestions + ")");
            return null;
        }
        
        Question question = questions.get(currentIndex);
        System.out.println("Retrieved question: " + (question != null ? question.getId() : "null"));
        return question;
    }
    
    /**
     * Submit answer and move to next question
     */
    public boolean submitAnswer(int selectedAnswerIndex, long timeSpent) {
        System.out.println("=== submitAnswer called ===");
        System.out.println("selectedAnswerIndex: " + selectedAnswerIndex);
        System.out.println("timeSpent: " + timeSpent);
        
        if (currentSession == null) {
            System.out.println("Returning false: currentSession is null");
            return false;
        }
        
        Question currentQuestion = getCurrentQuestion();
        if (currentQuestion == null) {
            System.out.println("Returning false: currentQuestion is null");
            return false;
        }
        
        System.out.println("Current question ID: " + currentQuestion.getId());
        System.out.println("Current session index before increment: " + currentSession.getCurrentQuestionIndex());
        
        boolean isCorrect = currentQuestion.isCorrectAnswer(selectedAnswerIndex);
        System.out.println("Answer is correct: " + isCorrect);
        
        // Create user answer and push to our custom stack
        UserAnswer userAnswer = new UserAnswer(
            currentQuestion.getId(),
            selectedAnswerIndex,
            isCorrect,
            timeSpent
        );
        userAnswers.push(userAnswer);
        System.out.println("User answer pushed to stack");
        
        // Update session score
        if (isCorrect) {
            currentSession.addScore(currentQuestion.getPoints());
            System.out.println("Score updated. New score: " + currentSession.getScore());
        }
        
        // Move to next question
        currentSession.nextQuestion();
        System.out.println("Current session index after increment: " + currentSession.getCurrentQuestionIndex());
        
        // Check if quiz is completed
        if (currentSession.getCurrentQuestionIndex() >= questions.size()) {
            currentSession.completeQuiz();
            System.out.println("Quiz completed!");
        }
        
        return isCorrect;
    }
    
    /**
     * Get previous answer (using stack peek)
     */
    public UserAnswer getPreviousAnswer() {
        if (!userAnswers.isEmpty()) {
            return userAnswers.peek();
        }
        return null;
    }
    
    /**
     * Undo last answer (using stack pop)
     */
    public UserAnswer undoLastAnswer() {
        if (!userAnswers.isEmpty()) {
            UserAnswer lastAnswer = userAnswers.pop();
            
            // Move back one question if possible
            if (currentSession.getCurrentQuestionIndex() > 0) {
                currentSession.setCurrentQuestionIndex(currentSession.getCurrentQuestionIndex() - 1);
                
                // Subtract points if it was correct
                Question question = questions.get(lastAnswer.getQuestionId() - 1);
                if (lastAnswer.isCorrect()) {
                    currentSession.setScore(currentSession.getScore() - question.getPoints());
                    currentSession.setCorrectAnswers(currentSession.getCorrectAnswers() - 1);
                }
            }
            
            return lastAnswer;
        }
        return null;
    }
    
    /**
     * Get quiz results
     */
    public QuizSession getResults() {
        return currentSession;
    }
    
    /**
     * Reset quiz for new session
     */
    public void resetQuiz() {
        currentSession = null;
        userAnswers.clear();
    }
    
    /**
     * Get total number of questions
     */
    public int getTotalQuestions() {
        return questions.size();
    }
    
    /**
     * Get all answered questions (convert stack to array)
     */
    public UserAnswer[] getAllAnswers() {
        UserAnswer[] answers = new UserAnswer[userAnswers.size()];
        QuizStack<UserAnswer> tempStack = new QuizStack<>();
        
        // Pop all answers to temporary stack (to maintain order)
        int index = userAnswers.size() - 1;
        while (!userAnswers.isEmpty()) {
            UserAnswer answer = userAnswers.pop();
            answers[index--] = answer;
            tempStack.push(answer);
        }
        
        // Restore original stack
        while (!tempStack.isEmpty()) {
            userAnswers.push(tempStack.pop());
        }
        
        return answers;
    }
    
    /**
     * Get current session
     */
    public QuizSession getCurrentSession() {
        return currentSession;
    }
    
    /**
     * Check if quiz is in progress
     */
    public boolean isQuizInProgress() {
        return currentSession != null && !currentSession.isCompleted();
    }
}