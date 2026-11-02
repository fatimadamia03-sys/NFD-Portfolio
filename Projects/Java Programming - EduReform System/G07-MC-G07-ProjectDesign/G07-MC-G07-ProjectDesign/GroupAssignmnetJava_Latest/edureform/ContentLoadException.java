
/**
 * contributed by: Nur Fatima Damia binti Mohammad Yazis (107468)
 * Description: Custom exception to handle content fetching errors file.
 */

public class ContentLoadException extends Exception {
    // Constructor that takes a message - called when we want to trigger the error.
    public ContentLoadException(String message) {
        // Pass our custom message up to the parent Exception class
        super(message);
    }
}