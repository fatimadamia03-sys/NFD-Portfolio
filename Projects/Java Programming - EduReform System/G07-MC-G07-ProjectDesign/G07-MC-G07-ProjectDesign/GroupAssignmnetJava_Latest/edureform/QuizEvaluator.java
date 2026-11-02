/**
 * QuizEvaluator contains utility methods for translating a quiz percentage
 * into human-readable feedback like badges, letter grades, and score scale text.
 */
public class QuizEvaluator {

    public static String getBadge(double percentage) {
        if (percentage >= 90.0) {
            return "Outstanding!";
        } else if (percentage >= 75.0) {
            return "Great job!";
        } else if (percentage >= 60.0) {
            return "Good effort.";
        } else if (percentage >= 40.0) {
            return "Needs improvement.";
        } else {
            return "Keep trying!";
        }
    }

    public static String getGrade(double percentage) {
        // Determine the final letter grade based on quiz performance percentage.
        if (percentage >= 90.0) {
            return "A";
        } else if (percentage >= 75.0) {
            return "B";
        } else if (percentage >= 60.0) {
            return "C";
        } else if (percentage >= 40.0) {
            return "D";
        } else {
            return "F";
        }
    }

    public static String getScoringScale(double percentage) {
        // Provide a descriptive performance bracket for the percentage score.
        if (percentage >= 90.0) {
            return "90-100%: Excellent mastery";
        } else if (percentage >= 75.0) {
            return "75-89%: Strong understanding";
        } else if (percentage >= 60.0) {
            return "60-74%: Competent";
        } else if (percentage >= 40.0) {
            return "40-59%: Basic awareness";
        } else {
            return "0-39%: Needs further study";
        }
    }
}
