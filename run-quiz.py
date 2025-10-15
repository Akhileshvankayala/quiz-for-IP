#!/usr/bin/env python3
"""
Interactive Java Quiz Application Runner
Complete automation script for running the quiz application
"""

import os
import sys
import subprocess
import platform

def print_header():
    print("=" * 50)
    print("    Interactive Java Quiz Application Runner")
    print("=" * 50)
    print()

def check_directory():
    """Check if we're in the correct project directory"""
    if not os.path.exists("backend/src/main/java") and not os.path.exists("backend\\src\\main\\java"):
        print("❌ ERROR: Not in the correct project directory!")
        print("Please navigate to the IP-Quiz project root directory first.")
        print("Expected structure: backend/src/main/java/com/quiz/")
        input("Press Enter to exit...")
        sys.exit(1)
    print("✅ Directory structure verified")

def check_java():
    """Check if Java is installed"""
    print("[1/4] Checking Java installation...")
    try:
        # Check Java runtime
        java_result = subprocess.run(['java', '-version'], 
                                   capture_output=True, text=True, check=True)
        
        # Check Java compiler
        javac_result = subprocess.run(['javac', '-version'], 
                                    capture_output=True, text=True, check=True)
        
        print("✅ Java installation verified")
        return True
    except (subprocess.CalledProcessError, FileNotFoundError):
        print("❌ ERROR: Java is not installed or not in PATH!")
        print("Please install Java JDK 8 or higher.")
        input("Press Enter to exit...")
        sys.exit(1)

def create_directories():
    """Create necessary output directories"""
    print("\n[2/4] Creating output directory...")
    
    # Change to backend directory
    os.chdir("backend")
    
    # Create bin directories
    os.makedirs("bin/com/quiz", exist_ok=True)
    print("✅ Output directory created")

def compile_java():
    """Compile all Java source files"""
    print("\n[3/4] Compiling Java source files...")
    
    # Java source files to compile
    java_files = [
        "src/main/java/com/quiz/QuizApplication.java",
        "src/main/java/com/quiz/controllers/QuizController.java",
        "src/main/java/com/quiz/datastructures/QuizLinkedList.java",
        "src/main/java/com/quiz/datastructures/QuizStack.java",
        "src/main/java/com/quiz/models/Question.java",
        "src/main/java/com/quiz/models/QuizSession.java",
        "src/main/java/com/quiz/models/UserAnswer.java",
        "src/main/java/com/quiz/services/QuizService.java"
    ]
    
    # Compile command
    compile_cmd = ['javac', '-d', 'bin', '-cp', 'src/main/java'] + java_files
    
    try:
        result = subprocess.run(compile_cmd, capture_output=True, text=True, check=True)
        print("✅ Compilation successful")
        return True
    except subprocess.CalledProcessError as e:
        print("❌ ERROR: Compilation failed!")
        print("Error output:")
        print(e.stderr)
        input("Press Enter to exit...")
        sys.exit(1)

def start_server():
    """Start the quiz application server"""
    print("\n[4/4] Starting the Quiz Application Server...")
    print()
    print("=" * 50)
    print("          🚀 STARTING QUIZ SERVER 🚀")
    print("=" * 50)
    print()
    print("Server will start at: http://localhost:8080")
    print("Open your web browser and navigate to the URL above")
    print("Press Ctrl+C to stop the server when done")
    print()
    print("=" * 50)
    
    try:
        # Start the Java application
        subprocess.run(['java', '-cp', 'bin', 'com.quiz.QuizApplication'], check=True)
    except KeyboardInterrupt:
        print("\n\n🛑 Server stopped by user")
    except subprocess.CalledProcessError as e:
        print(f"\n❌ ERROR: Server failed to start! Exit code: {e.returncode}")
        input("Press Enter to exit...")
        sys.exit(1)
    finally:
        print("\n✅ Server stopped. Thank you for using Java Quiz Application!")
        input("Press Enter to exit...")

def main():
    """Main execution function"""
    print_header()
    check_directory()
    check_java()
    create_directories()
    compile_java()
    start_server()

if __name__ == "__main__":
    main()