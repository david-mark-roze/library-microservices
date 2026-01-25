package au.com.library.loan.kafka.event;

/**
 * Represents the types of events related to loans that can be published to Kafka.
 * A value is associated with each event type to include in the header of the Kafka message.
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
