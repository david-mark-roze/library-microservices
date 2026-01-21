package au.com.library.loan.kafka.event;

/**
 * Represents an event indicating that a loaned edition copy has been reported as lost.
 *
 * @param context The context of the loan event.
 */
public record LoanLostEvent(LoanEventContext context) {
}
