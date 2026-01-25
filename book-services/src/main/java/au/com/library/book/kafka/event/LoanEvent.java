package au.com.library.book.kafka.event;

/**
 * Represents an event related to a loan, including its type and context.
 */
public record LoanEvent(LoanEventType eventType, LoanEventContext context) {
}
