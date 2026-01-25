package au.com.library.book.kafka.event;

/**
 * Represents the event types received from the Loan Service.
 */
public enum LoanEventType {
    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#LOANED loaned}.    */
    LOAN_CREATED,
    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available} because a loan has been returned.
     */
    LOAN_RETURNED,
    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#LOST lost}.
     */
    LOAN_MARKED_LOST
}
