# Use OpenJDK 11 as base image
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy backend files
COPY backend/ ./backend/

# Set working directory to backend
WORKDIR /app/backend

# Expose port 8080
EXPOSE 8080

# Run the application
CMD ["java", "-cp", "bin", "com.quiz.QuizApplication"]