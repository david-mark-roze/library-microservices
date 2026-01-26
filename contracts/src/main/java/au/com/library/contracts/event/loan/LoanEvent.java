package au.com.library.contracts.event.loan;

/**
 * Represents a loan event - including the event type and its associated context - that can be published to Kafka and received by subscribers.
 *
 * @see LoanEventContext
 * @see LoanEventType
 *
 * @param eventType The type of the loan event.
 * @param context   The context of the loan event.
 */
public record LoanEvent(LoanEventType eventType, LoanEventContext context) {
}
