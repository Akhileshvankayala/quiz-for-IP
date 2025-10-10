# Interactive Single-Player Quiz Application

A comprehensive Java-based quiz application featuring custom data structures and a modern web interface.

## 🎯 Features

- **10 Java Programming Questions** - Covering various difficulty levels from Easy to Hard
- **Custom Data Structures** - Implementation of Stack and LinkedList from scratch
- **Timer System** - 30-second countdown for each question
- **Fun Facts** - Educational trivia after each answer
- **Score System** - Points calculation with time bonus
- **Professional UI** - Modern design with dark/light theme toggle
- **Progress Tracking** - Visual progress bar and question counter
- **Results Screen** - Comprehensive performance analysis
- **Restart Functionality** - Complete quiz cycle with restart option

## 🛠️ Technology Stack

### Backend
- **Java** - Core backend development
- **Custom HTTP Server** - Built using `com.sun.net.httpserver.HttpServer`
- **MVC Architecture** - Organized code structure
- **Custom Data Structures**:
  - `QuizStack<T>` - For answer history and navigation
  - `QuizLinkedList<T>` - For question storage and management

### Frontend
- **HTML5** - Modern semantic markup
- **CSS3** - Responsive design with CSS Grid and Flexbox
- **JavaScript (ES6+)** - Interactive functionality and API communication
- **Font Awesome** - Icon library for enhanced UI

## 🏗️ Project Structure

```
IP-Quiz/
├── backend/
│   ├── src/main/java/com/quiz/
│   │   ├── QuizApplication.java          # Main application entry point
│   │   ├── controllers/
│   │   │   └── QuizController.java       # HTTP request handlers
│   │   ├── services/
│   │   │   └── QuizService.java          # Business logic
│   │   ├── models/
│   │   │   ├── Question.java             # Question data model
│   │   │   ├── QuizSession.java          # Session management
│   │   │   └── UserAnswer.java           # Answer tracking
│   │   └── datastructures/
│   │       ├── QuizStack.java            # Custom Stack implementation
│   │       └── QuizLinkedList.java       # Custom LinkedList implementation
│   ├── bin/                              # Compiled Java classes
│   └── run.bat                           # Windows batch script
└── frontend/
    ├── index.html                        # Main HTML file
    ├── style.css                         # Styling and themes
    └── script.js                         # Frontend logic and API calls
```

## 🚀 Getting Started

### Prerequisites
- Java JDK 8 or higher
- Modern web browser
- Git (for cloning)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/Akhileshvankayala/quiz-for-IP.git
   cd quiz-for-IP
   ```

2. **Compile the Java backend**
   ```bash
   javac -d backend\bin -sourcepath backend\src\main\java backend\src\main\java\com\quiz\QuizApplication.java
   ```

3. **Start the server**
   ```bash
   java -cp "backend\bin" com.quiz.QuizApplication
   ```

4. **Access the application**
   Open your browser and navigate to: `http://localhost:8080`

## 🎮 How to Play

1. **Enter your name** on the login screen
2. **Answer questions** - You have 30 seconds per question
3. **Learn from fun facts** - Read interesting trivia after each answer
4. **View your results** - See your final score, accuracy, and performance
5. **Play again** - Restart the quiz anytime with the "Take Quiz Again" button

## 📊 Scoring System

- **Correct Answer**: Base points per question
- **Time Bonus**: Extra points for quick answers
- **Final Score**: Total points + time bonuses
- **Performance Metrics**: Accuracy percentage and detailed breakdown

## 🎨 Features Showcase

- **Responsive Design** - Works on desktop and mobile devices
- **Theme Toggle** - Switch between light and dark modes
- **Loading States** - Smooth transitions and loading indicators
- **Error Handling** - Graceful error management and user feedback
- **Progress Tracking** - Visual indicators for quiz progress

## 🔧 Technical Highlights

### Custom Data Structures
- **QuizStack**: Implements LIFO operations for answer history
- **QuizLinkedList**: Dynamic storage for questions with efficient traversal

### Backend Architecture
- RESTful API design
- Session management
- JSON response formatting
- Static file serving

### Frontend Features
- Async/await for API calls
- Local state management
- Dynamic DOM manipulation
- CSS animations and transitions

## 📈 Future Enhancements

- [ ] Question categories and filtering
- [ ] Difficulty level selection
- [ ] Leaderboard system
- [ ] Question bank expansion
- [ ] User authentication
- [ ] Statistics dashboard

## 🤝 Contributing

Feel free to fork this project and submit pull requests for any improvements.

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Akhilesh Vankayala**
- GitHub: [@Akhileshvankayala](https://github.com/Akhileshvankayala)

---

⭐ **Star this repository if you found it helpful!** ⭐