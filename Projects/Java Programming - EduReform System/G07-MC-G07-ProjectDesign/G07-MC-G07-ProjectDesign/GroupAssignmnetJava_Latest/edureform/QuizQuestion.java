import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuizQuestion models a single quiz item.
 * It stores the question prompt, the list of possible answer options,
 * the correct answer text, and the question type.
 */
public class QuizQuestion {

    public enum Type {
        MULTIPLE_CHOICE,
        TRUE_FALSE
    }

    private final String prompt; // The text shown to the user.
    private final Type type; // The kind of quiz question being asked.
    private final List<String> options; // The selectable answer choices.
    private final String correctAnswer; // The validated correct choice.

    public QuizQuestion(String prompt, List<String> options, String correctAnswer, Type type) {
        this.prompt = prompt;
        this.type = type;
        this.options = Collections.unmodifiableList(options);
        this.correctAnswer = correctAnswer;
    }

    public String getPrompt() {
        return prompt;
    }

    public Type getType() {
        return type;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public boolean isCorrectAnswer(String answer) {
        // Compare the provided answer text against the stored correct answer.
        return answer != null && answer.trim().equalsIgnoreCase(correctAnswer.trim());
    }

    public static QuizQuestion createTrueFalseQuestion(String prompt, boolean correctAnswer) {
        return new QuizQuestion(prompt,
                Arrays.asList("True", "False"),
                correctAnswer ? "True" : "False",
                Type.TRUE_FALSE);
    }

    public static QuizQuestion createMultipleChoiceQuestion(String prompt, List<String> options, String correctAnswer) {
        // Factory method for building standard multiple-choice question objects.
        return new QuizQuestion(prompt, options, correctAnswer, Type.MULTIPLE_CHOICE);
    }
}
