package au.com.library.loan.kafka.event;

import lombok.Builder;

/**
 * Represents an event indicating that a loan has been created.
 *
 * @param context The context of the loan event.
 */
public record LoanCreatedEvent(LoanEventContext context) {
}
