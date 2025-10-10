package com.quiz;

import com.quiz.controllers.QuizController;
import com.quiz.services.QuizService;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main application class for the Interactive Single-Player Quiz Application
 * This serves as the entry point and HTTP server setup
 */
public class QuizApplication {
    private static final int PORT = 8080;
    
    public static void main(String[] args) {
        try {
            // Initialize the HTTP server
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            
            // Initialize services
            QuizService quizService = new QuizService();
            QuizController quizController = new QuizController(quizService);
            
            // Set up API endpoints
            server.createContext("/api/quiz/start", quizController::startQuiz);
            server.createContext("/api/quiz/question", quizController::getCurrentQuestion);
            server.createContext("/api/quiz/answer", quizController::submitAnswer);
            server.createContext("/api/quiz/results", quizController::getResults);
            server.createContext("/api/quiz/reset", quizController::resetQuiz);
            
            // Serve static files (HTML, CSS, JS)
            server.createContext("/", quizController::serveStaticFiles);
            
            // Enable CORS for all endpoints
            server.setExecutor(null);
            
            System.out.println("🚀 Quiz Application Server started on http://localhost:" + PORT);
            System.out.println("📚 Open your browser and navigate to the URL above to start the quiz!");
            System.out.println("💡 Features: Custom Data Structures (Stack & LinkedList), Java Questions, Timer, Fun Facts!");
            System.out.println("🎯 Press Ctrl+C to stop the server");
            
            server.start();
            
        } catch (IOException e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}