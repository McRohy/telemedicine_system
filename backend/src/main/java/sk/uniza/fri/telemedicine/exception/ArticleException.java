package sk.uniza.fri.telemedicine.exception;

/**
 * Exception thrown when a file operation fails.
 */
public class ArticleException extends RuntimeException {
    public ArticleException(String message) {
        super(message);
    }
}
