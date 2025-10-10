// Quiz Application JavaScript
// Handles all frontend interactions and API communication

class QuizApp {
    constructor() {
        this.currentQuestion = null;
        this.currentQuestionIndex = 0;
        this.totalQuestions = 10;
        this.score = 0;
        this.timer = null;
        this.timeLeft = 30;
        this.questionStartTime = null;
        this.selectedAnswer = null;
        this.isAnswerSubmitted = false;
        this.isLoadingQuestion = false; // Add loading lock
        this.isQuizCompleted = false; // Track quiz completion status
        
        // API base URL
        this.apiBase = '/api/quiz';
        
        // Initialize the application
        this.init();
    }
    
    init() {
        // Clear any refresh flags from previous sessions
        sessionStorage.removeItem('refreshed');
        
        this.bindEvents();
        this.initTheme();
        this.showScreen('login-screen');
    }
    
    bindEvents() {
        // Login form
        document.getElementById('login-form').addEventListener('submit', (e) => {
            e.preventDefault();
            this.startQuiz();
        });
        
        // Theme toggle
        document.getElementById('theme-btn').addEventListener('click', () => {
            this.toggleTheme();
        });
        
        // Submit answer button
        document.getElementById('submit-btn').addEventListener('click', () => {
            this.submitAnswer();
        });
        
        // Next question button
        document.getElementById('next-question-btn').addEventListener('click', () => {
            this.nextQuestion();
        });
        
        // Results screen buttons
        document.getElementById('restart-btn').addEventListener('click', () => {
            this.restartQuiz();
        });
        
        document.getElementById('review-btn').addEventListener('click', () => {
            this.reviewAnswers();
        });
        
        // Lifeline buttons (non-functional but interactive)
        document.getElementById('fifty-fifty-btn').addEventListener('click', () => {
            this.showToast('50-50 lifeline is a demo feature!', 'warning');
        });
        
        document.getElementById('hint-btn').addEventListener('click', () => {
            this.showToast('Hint feature is a demo placeholder!', 'warning');
        });
    }
    
    // Theme Management
    initTheme() {
        const savedTheme = localStorage.getItem('quiz-theme') || 'light';
        if (savedTheme === 'dark') {
            document.body.classList.add('dark-theme');
            document.getElementById('theme-btn').innerHTML = '<i class="fas fa-sun"></i>';
        }
    }
    
    toggleTheme() {
        document.body.classList.toggle('dark-theme');
        const isDark = document.body.classList.contains('dark-theme');
        document.getElementById('theme-btn').innerHTML = isDark ? 
            '<i class="fas fa-sun"></i>' : '<i class="fas fa-moon"></i>';
        localStorage.setItem('quiz-theme', isDark ? 'dark' : 'light');
    }
    
    // Screen Management
    showScreen(screenId) {
        document.querySelectorAll('.screen').forEach(screen => {
            screen.classList.remove('active');
        });
        document.getElementById(screenId).classList.add('active');
    }
    
    showLoading(show = true) {
        const overlay = document.getElementById('loading-overlay');
        if (show) {
            overlay.classList.add('active');
        } else {
            overlay.classList.remove('active');
        }
    }
    
    // Toast Notifications
    showToast(message, type = 'success') {
        const container = document.getElementById('toast-container');
        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        
        const icon = type === 'success' ? 'check-circle' : 
                    type === 'error' ? 'exclamation-circle' : 'exclamation-triangle';
        
        toast.innerHTML = `
            <i class="fas fa-${icon}"></i>
            <span>${message}</span>
        `;
        
        container.appendChild(toast);
        
        setTimeout(() => {
            toast.remove();
        }, 4000);
    }
    
    // Quiz Logic
    async startQuiz() {
        const playerName = document.getElementById('player-name').value.trim() || 'Anonymous Player';
        
        // Reset quiz completion status
        this.isQuizCompleted = false;
        
        this.showLoading(true);
        
        try {
            const response = await fetch(`${this.apiBase}/start`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `playerName=${encodeURIComponent(playerName)}`
            });
            
            const data = await response.json();
            
            if (data.success) {
                this.totalQuestions = data.totalQuestions;
                this.currentQuestionIndex = 0;
                this.score = 0;
                this.showScreen('quiz-screen');
                this.loadCurrentQuestion();
                this.showToast(`Welcome ${playerName}! Quiz started successfully!`, 'success');
            } else {
                this.showToast('Failed to start quiz. Please try again.', 'error');
            }
        } catch (error) {
            console.error('Error starting quiz:', error);
            this.showToast('Connection error. Please check if the server is running.', 'error');
        } finally {
            this.showLoading(false);
        }
    }
    
    async loadCurrentQuestion() {
        // Prevent double-triggering
        if (this.isLoadingQuestion) {
            console.log('Question loading already in progress, skipping...');
            return;
        }
        
        this.isLoadingQuestion = true;
        this.showLoading(true);
        
        try {
            console.log('Fetching from:', `${this.apiBase}/question`); // Debug log
            
            // Force fresh request with timestamp to avoid caching
            const timestamp = new Date().getTime();
            const response = await fetch(`${this.apiBase}/question?t=${timestamp}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                    'Cache-Control': 'no-cache, no-store, must-revalidate',
                    'Pragma': 'no-cache',
                    'Expires': '0'
                },
                cache: 'no-store'
            });
            
            console.log('Response status:', response.status); // Debug log
            console.log('Response ok:', response.ok); // Debug log
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            const data = await response.json();
            
            console.log('API Response:', data); // Debug log
            
            if (data.success && data.question) {
                this.currentQuestion = data.question;
                this.displayQuestion(data.question);
                this.updateProgress(data.question.questionNumber, data.question.totalQuestions);
                this.updateScore(data.question.currentScore);
                this.startTimer();
                this.selectedAnswer = null;
                this.isAnswerSubmitted = false;
                
                // Reset submit button
                const submitBtn = document.getElementById('submit-btn');
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<i class="fas fa-arrow-right"></i> Submit Answer';
            } else {
                console.error('API Response Error:', data); // Debug log
                if (data.error === 'No active quiz session or quiz completed') {
                    this.loadResults();
                } else {
                    this.showToast('Failed to load question: ' + (data.error || 'Unknown error'), 'error');
                }
            }
        } catch (error) {
            console.error('Error loading question:', error);
            console.error('Error details:', error.message, error.stack); // More debug info
            this.showToast('Failed to load question: ' + error.message, 'error');
        } finally {
            this.showLoading(false);
            this.isLoadingQuestion = false; // Release lock
        }
    }
    
    displayQuestion(question) {
        console.log('displayQuestion called with:', question); // Debug log
        
        document.getElementById('question-text').textContent = question.questionText || question.text;
        
        // Set difficulty badge
        const difficultyBadge = document.getElementById('difficulty-badge');
        difficultyBadge.textContent = question.difficulty;
        difficultyBadge.className = `difficulty-badge ${question.difficulty.toLowerCase()}`;
        
        // Generate options
        const optionsContainer = document.getElementById('options-container');
        optionsContainer.innerHTML = '';
        
        question.options.forEach((option, index) => {
            const optionElement = document.createElement('div');
            optionElement.className = 'option';
            optionElement.innerHTML = `
                <div class="option-letter">${String.fromCharCode(65 + index)}</div>
                <div class="option-text">${option}</div>
            `;
            
            optionElement.addEventListener('click', () => this.selectOption(index, optionElement));
            optionsContainer.appendChild(optionElement);
        });
    }
    
    selectOption(index, element) {
        if (this.isAnswerSubmitted) return;
        
        // Remove previous selection
        document.querySelectorAll('.option').forEach(opt => {
            opt.classList.remove('selected');
        });
        
        // Select current option
        element.classList.add('selected');
        this.selectedAnswer = index;
        
        // Enable submit button
        document.getElementById('submit-btn').disabled = false;
    }
    
    async submitAnswer() {
        if (this.selectedAnswer === null || this.isAnswerSubmitted) return;
        
        this.isAnswerSubmitted = true;
        this.stopTimer();
        
        const timeSpent = Date.now() - this.questionStartTime;
        
        this.showLoading(true);
        
        try {
            const response = await fetch(`${this.apiBase}/answer`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `selectedAnswer=${this.selectedAnswer}&timeSpent=${timeSpent}`
            });
            
            const data = await response.json();
            
            console.log('Answer submission response:', data); // Debug log
            
            if (data.success) {
                this.showAnswerResult(data);
                this.updateScore(data.score);
                
                if (data.isQuizCompleted) {
                    // Mark quiz as completed
                    this.isQuizCompleted = true;
                    // Quiz is completed, show results immediately after the modal closes
                    console.log('Quiz completed! Preparing to show results...');
                    setTimeout(() => {
                        console.log('Loading results now...');
                        this.loadResults();
                    }, 2000); // Reduced timeout to 2 seconds
                }
                
                // Fallback: Check if we just completed question 10
                if (this.currentQuestion && this.currentQuestion.questionNumber >= 10) {
                    console.log('Detected question 10 completion, marking as completed...');
                    this.isQuizCompleted = true;
                    setTimeout(() => {
                        console.log('Loading results after question 10...');
                        this.loadResults();
                    }, 2000);
                }
            } else {
                this.showToast('Failed to submit answer', 'error');
            }
        } catch (error) {
            console.error('Error submitting answer:', error);
            this.showToast('Failed to submit answer', 'error');
        } finally {
            this.showLoading(false);
        }
    }
    
    showAnswerResult(data) {
        const modal = document.getElementById('fun-fact-modal');
        const modalHeader = modal.querySelector('.modal-header');
        const statusElement = document.getElementById('answer-status');
        const correctAnswerInfo = document.getElementById('correct-answer-info');
        const funFactText = document.getElementById('fun-fact-text');
        
        // Update answer feedback
        if (data.isCorrect) {
            statusElement.textContent = 'Correct! 🎉';
            modalHeader.className = 'modal-header correct';
            correctAnswerInfo.innerHTML = `<p style="color: var(--success-color); font-weight: 500;">Great job! You got it right!</p>`;
        } else {
            statusElement.textContent = 'Incorrect 😔';
            modalHeader.className = 'modal-header incorrect';
            correctAnswerInfo.innerHTML = `
                <p style="color: var(--error-color); font-weight: 500;">
                    The correct answer was: <strong>${data.correctAnswerText}</strong>
                </p>
            `;
        }
        
        // Show fun fact
        funFactText.textContent = data.funFact;
        
        // Change button text if this is the final question
        const nextBtn = document.getElementById('next-question-btn');
        if (this.currentQuestion && this.currentQuestion.questionNumber >= 10) {
            nextBtn.innerHTML = '<i class="fas fa-flag-checkered"></i> View Results';
            
            // Auto-close modal and show results after 3 seconds for final question
            setTimeout(() => {
                modal.classList.remove('active');
                console.log('Auto-loading results after final question...');
                this.loadResults();
            }, 3000);
        } else {
            nextBtn.innerHTML = '<i class="fas fa-forward"></i> Next Question';
        }
        
        // Highlight correct and incorrect answers
        this.highlightAnswers(data.correctAnswer, this.selectedAnswer);
        
        // Show modal
        modal.classList.add('active');
    }
    
    highlightAnswers(correctIndex, selectedIndex) {
        const options = document.querySelectorAll('.option');
        
        options.forEach((option, index) => {
            if (index === correctIndex) {
                option.classList.add('correct');
            } else if (index === selectedIndex && selectedIndex !== correctIndex) {
                option.classList.add('incorrect');
            }
        });
    }
    
    nextQuestion() {
        const modal = document.getElementById('fun-fact-modal');
        modal.classList.remove('active');
        
        // Check if quiz is completed before trying to load next question
        if (this.isQuizCompleted) {
            console.log('Quiz is completed, loading results...');
            this.loadResults();
            return;
        }
        
        // Don't increment currentQuestionIndex here - the backend already handles this
        // when the answer is submitted
        this.loadCurrentQuestion();
    }
    
    // Timer Management
    startTimer() {
        this.timeLeft = 30;
        this.questionStartTime = Date.now();
        this.updateTimerDisplay();
        
        this.timer = setInterval(() => {
            this.timeLeft--;
            this.updateTimerDisplay();
            
            if (this.timeLeft <= 0) {
                this.timeUp();
            }
        }, 1000);
    }
    
    stopTimer() {
        if (this.timer) {
            clearInterval(this.timer);
            this.timer = null;
        }
    }
    
    updateTimerDisplay() {
        const timerElement = document.getElementById('timer');
        const timerStat = timerElement.closest('.timer-stat');
        
        timerElement.textContent = this.timeLeft;
        
        // Add warning classes based on time left
        timerStat.classList.remove('timer-warning', 'timer-danger');
        if (this.timeLeft <= 5) {
            timerStat.classList.add('timer-danger');
        } else if (this.timeLeft <= 10) {
            timerStat.classList.add('timer-warning');
        }
    }
    
    timeUp() {
        if (this.isAnswerSubmitted) return;
        
        this.showToast('Time\'s up! Moving to next question...', 'warning');
        
        // Auto-submit with no answer selected
        this.selectedAnswer = -1; // Invalid answer
        this.submitAnswer();
    }
    
    // Progress and Score Updates
    updateProgress(current, total) {
        const progressFill = document.getElementById('progress-fill');
        const questionCounter = document.getElementById('question-counter');
        
        const percentage = (current / total) * 100;
        progressFill.style.width = `${percentage}%`;
        questionCounter.textContent = `${current} / ${total}`;
    }
    
    updateScore(newScore) {
        this.score = newScore;
        document.getElementById('current-score').textContent = newScore;
    }
    
    // Results Management
    async loadResults() {
        this.showLoading(true);
        
        try {
            const response = await fetch(`${this.apiBase}/results`);
            const data = await response.json();
            
            if (data.success && data.results) {
                this.displayResults(data.results);
                this.showScreen('results-screen');
            } else {
                this.showToast('Failed to load results', 'error');
            }
        } catch (error) {
            console.error('Error loading results:', error);
            this.showToast('Failed to load results', 'error');
        } finally {
            this.showLoading(false);
        }
    }
    
    displayResults(results) {
        document.getElementById('final-score').textContent = results.finalScore;
        document.getElementById('correct-count').textContent = `${results.correctAnswers} / ${results.totalQuestions}`;
        document.getElementById('accuracy').textContent = `${results.accuracy.toFixed(1)}%`;
        document.getElementById('time-bonus').textContent = `+${results.timeBonus}`;
        
        // Create a simple performance chart
        this.createPerformanceChart(results);
        
        // Show completion message
        setTimeout(() => {
            this.showToast(`Quiz completed! You scored ${results.finalScore} points!`, 'success');
        }, 500);
    }
    
    createPerformanceChart(results) {
        const canvas = document.getElementById('results-chart');
        const ctx = canvas.getContext('2d');
        
        // Clear canvas
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        // Chart data
        const correct = results.correctAnswers;
        const incorrect = results.totalQuestions - results.correctAnswers;
        const total = results.totalQuestions;
        
        // Colors
        const correctColor = '#10b981';
        const incorrectColor = '#ef4444';
        
        // Draw pie chart
        const centerX = canvas.width / 2;
        const centerY = canvas.height / 2;
        const radius = 60;
        
        // Calculate angles
        const correctAngle = (correct / total) * 2 * Math.PI;
        const incorrectAngle = (incorrect / total) * 2 * Math.PI;
        
        // Draw correct slice
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, 0, correctAngle);
        ctx.closePath();
        ctx.fillStyle = correctColor;
        ctx.fill();
        
        // Draw incorrect slice
        ctx.beginPath();
        ctx.moveTo(centerX, centerY);
        ctx.arc(centerX, centerY, radius, correctAngle, correctAngle + incorrectAngle);
        ctx.closePath();
        ctx.fillStyle = incorrectColor;
        ctx.fill();
        
        // Add labels
        ctx.fillStyle = '#000';
        ctx.font = '12px Inter';
        ctx.textAlign = 'center';
        
        // Legend
        ctx.fillStyle = correctColor;
        ctx.fillRect(centerX - 80, centerY + 80, 15, 15);
        ctx.fillStyle = '#000';
        ctx.fillText(`Correct (${correct})`, centerX - 40, centerY + 92);
        
        ctx.fillStyle = incorrectColor;
        ctx.fillRect(centerX + 20, centerY + 80, 15, 15);
        ctx.fillStyle = '#000';
        ctx.fillText(`Incorrect (${incorrect})`, centerX + 60, centerY + 92);
    }
    
    async restartQuiz() {
        this.showLoading(true);
        
        try {
            await fetch(`${this.apiBase}/reset`, {
                method: 'POST'
            });
            
            // Reset local state
            this.currentQuestion = null;
            this.currentQuestionIndex = 0;
            this.score = 0;
            this.selectedAnswer = null;
            this.isAnswerSubmitted = false;
            this.isQuizCompleted = false; // Reset completion status
            this.stopTimer();
            
            // Clear form
            document.getElementById('player-name').value = '';
            
            // Show login screen
            this.showScreen('login-screen');
            this.showToast('Quiz reset! Ready for a new challenge!', 'success');
            
        } catch (error) {
            console.error('Error resetting quiz:', error);
            this.showToast('Failed to reset quiz', 'error');
        } finally {
            this.showLoading(false);
        }
    }
    
    reviewAnswers() {
        this.showToast('Answer review feature coming soon!', 'warning');
    }
}

// Initialize the quiz application when the page loads
document.addEventListener('DOMContentLoaded', () => {
    window.quizApp = new QuizApp();
});

// Handle page visibility changes (pause timer when tab is not active)
document.addEventListener('visibilitychange', () => {
    if (window.quizApp && window.quizApp.timer) {
        if (document.hidden) {
            // Page is hidden, pause timer
            window.quizApp.stopTimer();
        } else {
            // Page is visible again, resume timer
            if (!window.quizApp.isAnswerSubmitted && window.quizApp.timeLeft > 0) {
                window.quizApp.startTimer();
            }
        }
    }
});

// Handle browser back/forward buttons
window.addEventListener('popstate', (event) => {
    // Prevent user from navigating away during quiz
    if (window.quizApp && window.quizApp.timer) {
        window.history.pushState(null, null, window.location.href);
        window.quizApp.showToast('Please complete the quiz before leaving!', 'warning');
    }
});

// Add keyboard shortcuts
document.addEventListener('keydown', (event) => {
    if (!window.quizApp) return;
    
    // Number keys 1-4 for selecting options
    if (event.key >= '1' && event.key <= '4') {
        const optionIndex = parseInt(event.key) - 1;
        const options = document.querySelectorAll('.option');
        if (options[optionIndex] && !window.quizApp.isAnswerSubmitted) {
            options[optionIndex].click();
        }
    }
    
    // Enter key to submit answer
    if (event.key === 'Enter') {
        const submitBtn = document.getElementById('submit-btn');
        const nextBtn = document.getElementById('next-question-btn');
        
        if (submitBtn && !submitBtn.disabled && document.getElementById('quiz-screen').classList.contains('active')) {
            submitBtn.click();
        } else if (nextBtn && document.getElementById('fun-fact-modal').classList.contains('active')) {
            nextBtn.click();
        }
    }
    
    // Escape key to close modal
    if (event.key === 'Escape') {
        const modal = document.getElementById('fun-fact-modal');
        if (modal && modal.classList.contains('active')) {
            document.getElementById('next-question-btn').click();
        }
    }
});

// Prevent right-click context menu during quiz (optional)
document.addEventListener('contextmenu', (event) => {
    if (document.getElementById('quiz-screen').classList.contains('active')) {
        event.preventDefault();
    }
});