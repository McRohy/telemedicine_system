package sk.uniza.fri.telemedicine.exception;

/**
 * Exception thrown when a requested resource is not found in the database.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
