@echo off
echo ===============================================
echo    Interactive Java Quiz Application Runner
echo ===============================================
echo.

:: Check if we're in the right directory
if not exist "backend\src\main\java" (
    echo ERROR: Not in the correct project directory!
    echo Please navigate to the IP-Quiz project root directory first.
    echo Expected structure: backend\src\main\java\com\quiz\
    pause
    exit /b 1
)

:: Navigate to backend directory
cd backend

echo [1/4] Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH!
    echo Please install Java JDK 8 or higher.
    pause
    exit /b 1
)

javac -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java compiler ^(javac^) is not installed or not in PATH!
    echo Please install Java JDK 8 or higher.
    pause
    exit /b 1
)
echo ✓ Java installation verified

echo.
echo [2/4] Creating output directory...
if not exist "bin" mkdir bin
if not exist "bin\com" mkdir bin\com
if not exist "bin\com\quiz" mkdir bin\com\quiz
echo ✓ Output directory created

echo.
echo [3/4] Compiling Java source files...
javac -d bin -cp "src\main\java" ^
    "src\main\java\com\quiz\QuizApplication.java" ^
    "src\main\java\com\quiz\controllers\QuizController.java" ^
    "src\main\java\com\quiz\datastructures\QuizLinkedList.java" ^
    "src\main\java\com\quiz\datastructures\QuizStack.java" ^
    "src\main\java\com\quiz\models\Question.java" ^
    "src\main\java\com\quiz\models\QuizSession.java" ^
    "src\main\java\com\quiz\models\UserAnswer.java" ^
    "src\main\java\com\quiz\services\QuizService.java"

if errorlevel 1 (
    echo ERROR: Compilation failed! Check the error messages above.
    pause
    exit /b 1
)
echo ✓ Compilation successful

echo.
echo [4/4] Starting the Quiz Application Server...
echo.
echo ===============================================
echo          🚀 STARTING QUIZ SERVER 🚀
echo ===============================================
echo.
echo Server will start at: http://localhost:8080
echo Open your web browser and navigate to the URL above
echo Press Ctrl+C to stop the server when done
echo.
echo ===============================================

java -cp bin com.quiz.QuizApplication

echo.
echo Server stopped. Thank you for using Java Quiz Application!
pause