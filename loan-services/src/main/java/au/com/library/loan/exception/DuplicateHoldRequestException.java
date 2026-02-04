package au.com.library.loan.exception;

/**
 * Exception thrown when a duplicate hold request is made by a member for the same edition.
 */
public class DuplicateHoldRequestException extends RuntimeException {
    public DuplicateHoldRequestException(String message) {
        super(message);
    }
}
