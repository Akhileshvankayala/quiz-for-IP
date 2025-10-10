const http = require('http');
const fs = require('fs');
const path = require('path');
const url = require('url');

const PORT = process.env.PORT || 3000;

// MIME types for different file extensions
const mimeTypes = {
    '.html': 'text/html',
    '.js': 'text/javascript',
    '.css': 'text/css',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpg',
    '.gif': 'image/gif',
    '.ico': 'image/x-icon',
    '.svg': 'image/svg+xml'
};

// Mock quiz data for demonstration
const quizQuestions = [
    {
        questionNumber: 1,
        questionText: "What is the time complexity of binary search?",
        difficulty: "Medium",
        options: ["O(n)", "O(log n)", "O(n²)", "O(1)"],
        correctAnswer: 1,
        correctAnswerText: "O(log n)",
        funFact: "Binary search divides the search space in half with each comparison, making it very efficient for sorted arrays."
    },
    {
        questionNumber: 2,
        questionText: "Which data structure follows LIFO principle?",
        difficulty: "Easy",
        options: ["Queue", "Stack", "Array", "Linked List"],
        correctAnswer: 1,
        correctAnswerText: "Stack",
        funFact: "LIFO stands for Last In, First Out - like a stack of plates where you take from the top."
    },
    // Add more questions as needed
];

let currentQuestionIndex = 0;
let userAnswers = [];
let quizStarted = false;

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);
    const pathname = parsedUrl.pathname;

    // Enable CORS
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

    if (req.method === 'OPTIONS') {
        res.writeHead(200);
        res.end();
        return;
    }

    // API Routes
    if (pathname.startsWith('/api/quiz/')) {
        handleAPIRequest(req, res, pathname);
        return;
    }

    // Serve static files
    let filePath = path.join(__dirname, 'frontend', pathname === '/' ? 'index.html' : pathname);
    
    fs.readFile(filePath, (err, content) => {
        if (err) {
            if (err.code === 'ENOENT') {
                res.writeHead(404);
                res.end('Page not found');
            } else {
                res.writeHead(500);
                res.end('Server error');
            }
        } else {
            const ext = path.extname(filePath);
            const contentType = mimeTypes[ext] || 'application/octet-stream';
            
            res.writeHead(200, { 'Content-Type': contentType });
            res.end(content);
        }
    });
});

function handleAPIRequest(req, res, pathname) {
    res.setHeader('Content-Type', 'application/json');

    if (pathname === '/api/quiz/start' && req.method === 'POST') {
        quizStarted = true;
        currentQuestionIndex = 0;
        userAnswers = [];
        res.writeHead(200);
        res.end(JSON.stringify({ success: true }));
    }
    else if (pathname === '/api/quiz/question' && req.method === 'GET') {
        if (currentQuestionIndex < quizQuestions.length) {
            const question = quizQuestions[currentQuestionIndex];
            res.writeHead(200);
            res.end(JSON.stringify({ success: true, question }));
        } else {
            res.writeHead(200);
            res.end(JSON.stringify({ success: false, message: 'No more questions' }));
        }
    }
    else if (pathname === '/api/quiz/answer' && req.method === 'POST') {
        let body = '';
        req.on('data', chunk => body += chunk);
        req.on('end', () => {
            const formData = new URLSearchParams(body);
            const selectedAnswer = parseInt(formData.get('selectedAnswer'));
            const timeSpent = parseInt(formData.get('timeSpent'));

            const currentQuestion = quizQuestions[currentQuestionIndex];
            const isCorrect = selectedAnswer === currentQuestion.correctAnswer;
            
            userAnswers.push({
                questionNumber: currentQuestion.questionNumber,
                selectedAnswer,
                isCorrect,
                timeSpent
            });

            currentQuestionIndex++;
            
            res.writeHead(200);
            res.end(JSON.stringify({
                success: true,
                isCorrect,
                correctAnswer: currentQuestion.correctAnswer,
                correctAnswerText: currentQuestion.correctAnswerText,
                funFact: currentQuestion.funFact,
                score: isCorrect ? 100 : 0,
                isQuizCompleted: currentQuestionIndex >= quizQuestions.length
            }));
        });
    }
    else if (pathname === '/api/quiz/results' && req.method === 'GET') {
        const correctAnswers = userAnswers.filter(a => a.isCorrect).length;
        const totalQuestions = quizQuestions.length;
        const accuracy = (correctAnswers / totalQuestions) * 100;
        const finalScore = correctAnswers * 100;

        res.writeHead(200);
        res.end(JSON.stringify({
            success: true,
            results: {
                correctAnswers,
                totalQuestions,
                accuracy,
                finalScore,
                timeBonus: 0
            }
        }));
    }
    else if (pathname === '/api/quiz/reset' && req.method === 'POST') {
        quizStarted = false;
        currentQuestionIndex = 0;
        userAnswers = [];
        res.writeHead(200);
        res.end(JSON.stringify({ success: true }));
    }
    else {
        res.writeHead(404);
        res.end(JSON.stringify({ success: false, message: 'API endpoint not found' }));
    }
}

server.listen(PORT, () => {
    console.log(`🚀 Quiz Application Server started on port ${PORT}`);
    console.log(`📚 Open your browser and navigate to http://localhost:${PORT} to start the quiz!`);
});