package au.com.library.loan.exception;

/**
 * Exception thrown when an attempt is made to loan an edition copy that is unavailable.
 */
public class CopyUnavailableException extends RuntimeException {
    public CopyUnavailableException(String message) {
        super(message);
    }
}
