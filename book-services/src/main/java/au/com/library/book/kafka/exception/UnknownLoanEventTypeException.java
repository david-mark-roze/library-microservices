package au.com.library.book.kafka.exception;

/**
 * Exception thrown when an unknown loan event type is encountered when processing Kafka events related to edition copies.
 */
public class UnknownLoanEventTypeException extends RuntimeException {

    public UnknownLoanEventTypeException(String message) {
        super(message);
    }
}
