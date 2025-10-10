package com.quiz.controllers;

import com.quiz.models.Question;
import com.quiz.models.QuizSession;
import com.quiz.models.UserAnswer;
import com.quiz.services.QuizService;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * QuizController handles HTTP requests and responses for the quiz application
 * Provides REST API endpoints for frontend communication
 */
public class QuizController {
    private final QuizService quizService;
    
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    
    /**
     * Start a new quiz session
     * POST /api/quiz/start
     */
    public void startQuiz(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            enableCORS(exchange);
            
            // Parse request body for player name (optional)
            String requestBody = readRequestBody(exchange);
            Map<String, String> params = parseFormData(requestBody);
            String playerName = params.getOrDefault("playerName", "Anonymous Player");
            
            QuizSession session = quizService.startQuiz(playerName);
            
            String jsonResponse = String.format(
                "{\"success\": true, \"sessionId\": \"%s\", \"totalQuestions\": %d, \"message\": \"Quiz started successfully!\"}",
                session.getSessionId(),
                session.getTotalQuestions()
            );
            
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    /**
     * Get current question
     * GET /api/quiz/question
     */
    public void getCurrentQuestion(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            enableCORS(exchange);
            
            System.out.println("=== GET CURRENT QUESTION REQUEST ===");
            
            Question question = quizService.getCurrentQuestion();
            QuizSession session = quizService.getCurrentSession();
            
            System.out.println("Session: " + (session != null ? session.toString() : "null"));
            System.out.println("Question: " + (question != null ? question.toString() : "null"));
            
            if (session != null) {
                System.out.println("Current question index: " + session.getCurrentQuestionIndex());
                System.out.println("Total questions: " + quizService.getTotalQuestions());
            }
            
            if (question == null || session == null) {
                System.out.println("Returning 404: No active quiz session or quiz completed");
                sendJsonResponse(exchange, 404, "{\"error\": \"No active quiz session or quiz completed\"}");
                return;
            }
            
            String optionsJson = formatOptionsAsJson(question.getOptions());
            
            String jsonResponse = String.format(
                "{\"success\": true, \"question\": {\"id\": %d, \"text\": \"%s\", \"options\": %s, \"difficulty\": \"%s\", \"questionNumber\": %d, \"totalQuestions\": %d, \"currentScore\": %d}}",
                question.getId(),
                escapeJson(question.getQuestionText()),
                optionsJson,
                question.getDifficulty(),
                session.getCurrentQuestionIndex() + 1,
                session.getTotalQuestions(),
                session.getScore()
            );
            
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    /**
     * Submit answer for current question
     * POST /api/quiz/answer
     */
    public void submitAnswer(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            enableCORS(exchange);
            
            System.out.println("=== SUBMIT ANSWER REQUEST ===");
            
            String requestBody = readRequestBody(exchange);
            Map<String, String> params = parseFormData(requestBody);
            
            System.out.println("Request params: " + params);
            
            try {
                int selectedAnswer = Integer.parseInt(params.get("selectedAnswer"));
                long timeSpent = Long.parseLong(params.getOrDefault("timeSpent", "0"));
                
                System.out.println("Selected answer: " + selectedAnswer + ", Time spent: " + timeSpent);
                
                Question currentQuestion = quizService.getCurrentQuestion();
                if (currentQuestion == null) {
                    System.out.println("No current question available");
                    sendJsonResponse(exchange, 404, "{\"error\": \"No current question available\"}");
                    return;
                }
                
                System.out.println("Current question before submit: " + currentQuestion.getId());
                boolean isCorrect = quizService.submitAnswer(selectedAnswer, timeSpent);
                QuizSession session = quizService.getCurrentSession();
                
                System.out.println("Answer correct: " + isCorrect);
                System.out.println("Session after submit: " + (session != null ? session.toString() : "null"));
                
                String jsonResponse = String.format(
                    "{\"success\": true, \"isCorrect\": %s, \"correctAnswer\": %d, \"correctAnswerText\": \"%s\", \"funFact\": \"%s\", \"score\": %d, \"isQuizCompleted\": %s}",
                    isCorrect,
                    currentQuestion.getCorrectAnswerIndex(),
                    escapeJson(currentQuestion.getCorrectAnswer()),
                    escapeJson(currentQuestion.getFunFact()),
                    session.getScore(),
                    session.isCompleted()
                );
                
                sendJsonResponse(exchange, 200, jsonResponse);
                
            } catch (NumberFormatException e) {
                sendJsonResponse(exchange, 400, "{\"error\": \"Invalid answer format\"}");
            }
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    /**
     * Get quiz results
     * GET /api/quiz/results
     */
    public void getResults(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            enableCORS(exchange);
            
            QuizSession session = quizService.getResults();
            
            if (session == null) {
                sendJsonResponse(exchange, 404, "{\"error\": \"No quiz session found\"}");
                return;
            }
            
            UserAnswer[] answers = quizService.getAllAnswers();
            String answersJson = formatAnswersAsJson(answers);
            
            String jsonResponse = String.format(
                "{\"success\": true, \"results\": {\"sessionId\": \"%s\", \"playerName\": \"%s\", \"score\": %d, \"finalScore\": %d, \"correctAnswers\": %d, \"totalQuestions\": %d, \"accuracy\": %.1f, \"timeBonus\": %d, \"totalTimeSpent\": %d, \"isCompleted\": %s, \"answers\": %s}}",
                session.getSessionId(),
                session.getPlayerName() != null ? escapeJson(session.getPlayerName()) : "Anonymous",
                session.getScore(),
                session.getFinalScore(),
                session.getCorrectAnswers(),
                session.getTotalQuestions(),
                session.getAccuracy(),
                session.getTimeBonus(),
                session.getTotalTimeSpent(),
                session.isCompleted(),
                answersJson
            );
            
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    /**
     * Reset quiz session
     * POST /api/quiz/reset
     */
    public void resetQuiz(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            enableCORS(exchange);
            
            quizService.resetQuiz();
            
            String jsonResponse = "{\"success\": true, \"message\": \"Quiz reset successfully\"}";
            sendJsonResponse(exchange, 200, jsonResponse);
        } else {
            sendJsonResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
        }
    }
    
    /**
     * Serve static files (HTML, CSS, JS)
     */
    public void serveStaticFiles(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        
        if (path.equals("/")) {
            path = "/index.html";
        }
        
        try {
            // Get the frontend directory path
            String projectRoot = System.getProperty("user.dir");
            System.out.println("Current working directory: " + projectRoot);
            
            // Ensure we're working from the project root
            if (projectRoot.endsWith("backend")) {
                projectRoot = projectRoot.substring(0, projectRoot.lastIndexOf("backend"));
            }
            
            String frontendDir = projectRoot + File.separator + "frontend";
            System.out.println("Frontend directory: " + frontendDir);
            
            String requestedFile = path.replace("/", File.separator);
            Path filePath = Paths.get(frontendDir + requestedFile);
            System.out.println("Requested file path: " + filePath.toString());
            
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                String contentType = getContentType(path);
                exchange.getResponseHeaders().set("Content-Type", contentType);
                
                byte[] fileContent = Files.readAllBytes(filePath);
                exchange.sendResponseHeaders(200, fileContent.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileContent);
                }
                System.out.println("Served file: " + filePath);
            } else {
                // File not found
                System.out.println("File not found: " + filePath);
                String notFoundResponse = "<html><body><h1>404 - File Not Found</h1><p>The requested file was not found: " + path + "</p><p>Looking in: " + frontendDir + "</p></body></html>";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(404, notFoundResponse.length());
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFoundResponse.getBytes());
                }
            }
        } catch (Exception e) {
            System.err.println("Error serving static file: " + e.getMessage());
            e.printStackTrace();
            
            // Send error response
            String errorResponse = "<html><body><h1>500 - Internal Server Error</h1><p>Error: " + e.getMessage() + "</p></body></html>";
            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(500, errorResponse.length());
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorResponse.getBytes());
            }
        }
    }
    
    // Helper methods
    
    private void enableCORS(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, String jsonResponse) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, jsonResponse.length());
        
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(jsonResponse.getBytes());
        }
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
            return body.toString();
        }
    }
    
    private Map<String, String> parseFormData(String formData) {
        Map<String, String> params = new HashMap<>();
        if (formData != null && !formData.isEmpty()) {
            String[] pairs = formData.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    params.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return params;
    }
    
    private String formatOptionsAsJson(String[] options) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < options.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append("\"").append(escapeJson(options[i])).append("\"");
        }
        sb.append("]");
        return sb.toString();
    }
    
    private String formatAnswersAsJson(UserAnswer[] answers) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < answers.length; i++) {
            if (i > 0) sb.append(", ");
            UserAnswer answer = answers[i];
            sb.append(String.format(
                "{\"questionId\": %d, \"selectedAnswer\": %d, \"isCorrect\": %s, \"timeSpent\": %d}",
                answer.getQuestionId(),
                answer.getSelectedAnswerIndex(),
                answer.isCorrect(),
                answer.getTimeSpent()
            ));
        }
        sb.append("]");
        return sb.toString();
    }
    
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
    }
    
    private String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".json")) return "application/json";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        return "text/plain";
    }
}