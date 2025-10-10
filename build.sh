#!/bin/bash

# Build script for Render deployment
echo "Starting build process..."

# Navigate to backend directory
cd backend

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile Java files
echo "Compiling Java files..."
javac -d bin -cp src/main/java $(find src/main/java -name "*.java")

echo "Build completed successfully!"