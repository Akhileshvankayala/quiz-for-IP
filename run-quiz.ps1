# Interactive Java Quiz Application Runner (PowerShell)
# Complete automation script for running the quiz application

Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "    Interactive Java Quiz Application Runner" -ForegroundColor Yellow
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Check if we're in the right directory
if (-not (Test-Path "backend\src\main\java")) {
    Write-Host "ERROR: Not in the correct project directory!" -ForegroundColor Red
    Write-Host "Please navigate to the IP-Quiz project root directory first." -ForegroundColor Red
    Write-Host "Expected structure: backend\src\main\java\com\quiz\" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Navigate to backend directory
Set-Location backend

Write-Host "[1/4] Checking Java installation..." -ForegroundColor Green
try {
    $javaVersion = java -version 2>&1
    $javacVersion = javac -version 2>&1
    Write-Host "✓ Java installation verified" -ForegroundColor Green
    Write-Host "Java Version: $($javaVersion[0])" -ForegroundColor Gray
}
catch {
    Write-Host "ERROR: Java is not installed or not in PATH!" -ForegroundColor Red
    Write-Host "Please install Java JDK 8 or higher." -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[2/4] Creating output directory..." -ForegroundColor Green
if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" -Force | Out-Null }
if (-not (Test-Path "bin\com")) { New-Item -ItemType Directory -Path "bin\com" -Force | Out-Null }
if (-not (Test-Path "bin\com\quiz")) { New-Item -ItemType Directory -Path "bin\com\quiz" -Force | Out-Null }
Write-Host "✓ Output directory created" -ForegroundColor Green

Write-Host ""
Write-Host "[3/4] Compiling Java source files..." -ForegroundColor Green

$compileCommand = @"
javac -d bin -cp "src\main\java" "src\main\java\com\quiz\QuizApplication.java" "src\main\java\com\quiz\controllers\QuizController.java" "src\main\java\com\quiz\datastructures\QuizLinkedList.java" "src\main\java\com\quiz\datastructures\QuizStack.java" "src\main\java\com\quiz\models\Question.java" "src\main\java\com\quiz\models\QuizSession.java" "src\main\java\com\quiz\models\UserAnswer.java" "src\main\java\com\quiz\services\QuizService.java"
"@

$result = Invoke-Expression $compileCommand 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Compilation failed!" -ForegroundColor Red
    Write-Host $result -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}
Write-Host "✓ Compilation successful" -ForegroundColor Green

Write-Host ""
Write-Host "[4/4] Starting the Quiz Application Server..." -ForegroundColor Green
Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "          🚀 STARTING QUIZ SERVER 🚀" -ForegroundColor Yellow
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Server will start at: " -NoNewline -ForegroundColor White
Write-Host "http://localhost:8080" -ForegroundColor Blue
Write-Host "Open your web browser and navigate to the URL above" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server when done" -ForegroundColor Gray
Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan

# Start the application
java -cp bin com.quiz.QuizApplication

Write-Host ""
Write-Host "Server stopped. Thank you for using Java Quiz Application!" -ForegroundColor Green
Read-Host "Press Enter to exit"