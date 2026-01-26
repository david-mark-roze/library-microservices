package au.com.library.contracts.event.loan;

/**
 * Represents the types of events related to loans that can be published to Kafka and received by subscribers.
 */
public enum LoanEventType {
    /**
     * Event type for when a loan is created.
     */
    LOAN_CREATED,
    /**
     * Event type for when a loan is returned.
     */
    LOAN_RETURNED,
    /**
     * Event type for when a loan is marked as lost.
     */
    LOAN_MARKED_LOST;
}
