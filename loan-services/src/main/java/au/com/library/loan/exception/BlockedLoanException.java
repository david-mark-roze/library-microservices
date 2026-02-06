package au.com.library.loan.exception;

/**
 * Exception thrown when a loan cannot be processed due to the member being blocked, such as because of a hold request.
 */
public class BlockedLoanException extends RuntimeException {
    public BlockedLoanException(String message) {
        super(message);
    }
}
