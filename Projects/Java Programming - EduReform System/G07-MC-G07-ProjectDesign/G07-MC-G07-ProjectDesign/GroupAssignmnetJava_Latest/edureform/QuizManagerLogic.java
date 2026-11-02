import java.util.ArrayList;
import java.util.List;

/**
 * QuizManagerLogic owns the quiz data and state for the quiz module.
 * It builds the question bank, tracks the current question index and score,
 * and provides helper methods for submitting answers, advancing through questions,
 * and calculating the final percentage.
 */
public class QuizManagerLogic {

        // The immutable list of question objects that make up the quiz.
        private final List<QuizQuestion> questions;
        // The current position inside the question list.
        private int currentIndex;
        // The cumulative number of correctly answered questions.
        private int score;
        
        private long questionStartTimeMs; // The small stopwatch
        private long quizStartTimeMs;     // The big stopwatch

        public GamificationHub myHub;

        public QuizManagerLogic() {
                this.questions = buildQuestionBank();
                this.currentIndex = 0;
                this.score = 0;
                this.questionStartTimeMs = System.currentTimeMillis();
                this.quizStartTimeMs = System.currentTimeMillis(); 
        }

        /**
         * Build the quiz question bank using QuizQuestion objects.
         * Every question object encapsulates its prompt, answer options,
         * correct answer, and question type.
         */
        private List<QuizQuestion> buildQuestionBank() {
                List<QuizQuestion> bank = new ArrayList<>();

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "What does SDG 4 primarily focus on?",
                        java.util.Arrays.asList("Quality education", "Clean water access", "Good health and well-being", "Affordable energy"),
                        "Quality education"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Access to free primary schooling helps achieve quality education.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which of the following is a key element of quality education?",
                        java.util.Arrays.asList("Trained teachers", "Individual fashion trends", "Celebrity endorsements", "Movie ratings"),
                        "Trained teachers"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Inclusive classrooms welcome students of all backgrounds.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which practice helps improve literacy skills?",
                        java.util.Arrays.asList("Reading daily", "Skipping class", "Watching TV all day", "Memorizing numbers only"),
                        "Reading daily"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Education policies that reduce class size support learning.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which service is most directly linked to school attendance?",
                        java.util.Arrays.asList("School transportation", "Space travel", "Luxury cosmetics", "Movie streaming"),
                        "School transportation"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Homework should always be completed without any support.",
                        false));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "What is an example of lifelong learning?",
                        java.util.Arrays.asList("Learning a new skill at any age", "Graduating once", "Vacationing without study", "Only learning in childhood"),
                        "Learning a new skill at any age"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Quality education includes critical thinking and creativity.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "A school with well-trained teachers is an example of:",
                        java.util.Arrays.asList("Quality education environment", "Financial burden", "Transport issue", "Health hazard"),
                        "Quality education environment"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Early childhood learning can improve later academic success.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which is a supportive classroom behavior?",
                        java.util.Arrays.asList("Listening to classmates", "Ignoring the teacher", "Talking over others", "Skipping group work"),
                        "Listening to classmates"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Access to books and learning materials is part of quality education.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which action supports equitable education?",
                        java.util.Arrays.asList("Providing scholarships", "Charging excessive fees", "Limiting access", "Ignoring special needs"),
                        "Providing scholarships"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Digital literacy is important for modern education.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "What is an example of an inclusive education practice?",
                        java.util.Arrays.asList("Adapting lessons for different learners", "Teaching only one style", "Excluding students with disabilities", "Using a single textbook"),
                        "Adapting lessons for different learners"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Teachers help students develop both knowledge and skills.",
                        true));

                bank.add(QuizQuestion.createMultipleChoiceQuestion(
                        "Which habit supports successful studying?",
                        java.util.Arrays.asList("Organizing notes", "Cramming at the last minute", "Procrastinating", "Avoiding review"),
                        "Organizing notes"));

                bank.add(QuizQuestion.createTrueFalseQuestion(
                        "Education empowers individuals to pursue better opportunities.",
                        true));

                return bank;
        }

        public QuizQuestion getCurrentQuestion() {
                if (currentIndex < 0 || currentIndex >= questions.size()) {
                        return null;
                }
                return questions.get(currentIndex);
        }

        public int getCurrentIndex() {
                return currentIndex;
        }

        public int getTotalQuestions() {
                return questions.size();
        }

        public int getScore() {
                return score;
        }

        public boolean isLastQuestion() {
                return currentIndex == questions.size() - 1;
        }

        public boolean submitAnswer(String answer) {
                QuizQuestion currentQuestion = getCurrentQuestion();
                boolean correct = currentQuestion != null && currentQuestion.isCorrectAnswer(answer);

                if (correct) {
                        score++;
                        // Check the SMALL stopwatch for the speed badges
                        long timeTaken = System.currentTimeMillis() - questionStartTimeMs;
                        int secondsTaken = (int) (timeTaken / 1000);
                        myHub.checkTime(secondsTaken);
                }

                myHub.answerQuestion(correct);
                return correct;
        }

        public void moveToNextQuestion() {
                // Advance the active question pointer when the quiz is still in progress.
                if (currentIndex < questions.size() - 1) {
                        currentIndex++;
                }
        }

        public void resetQuiz() {
                currentIndex = 0;
                score = 0;
                // Start BOTH stopwatches when a brand new quiz begins
                questionStartTimeMs = System.currentTimeMillis();
                quizStartTimeMs = System.currentTimeMillis(); 
        }

        public void startQuestionTimer() {
                // ONLY the small stopwatch goes here!
                questionStartTimeMs = System.currentTimeMillis(); 
        }

        public long getElapsedTimeSinceQuestionStarted() {
                return System.currentTimeMillis() - quizStartTimeMs;
        }

        public double getPercentage() {
                if (questions.isEmpty()) {
                        return 0.0;
                }
                return 100.0 * score / questions.size();
        }

        public void finishQuizSession() {
                // Check the BIG stopwatch for the overall time
                long totalTimeMs = System.currentTimeMillis() - quizStartTimeMs;
                int totalQuizSeconds = (int) (totalTimeMs / 1000);

                // Pass the overall time to the Hub
                myHub.finishQuiz(score, questions.size(), totalQuizSeconds);

                myHub.printEverything();

                try {
                        myHub.saveProgress();
                } catch (Exception e) {
                        System.out.println("Oops! Could not save the file.");
                }
        }
}