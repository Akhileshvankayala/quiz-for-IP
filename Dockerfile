# Use OpenJDK 11 with build tools
FROM openjdk:11-jdk-slim

# Install necessary build tools
RUN apt-get update && apt-get install -y \
    findutils \
    && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Set working directory to backend and compile Java files
WORKDIR /app/backend

# Create bin directory and compile Java files
RUN mkdir -p bin && \
    find src/main/java -name "*.java" -exec javac -d bin -cp src/main/java {} + 

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-cp", "bin", "com.quiz.QuizApplication"]