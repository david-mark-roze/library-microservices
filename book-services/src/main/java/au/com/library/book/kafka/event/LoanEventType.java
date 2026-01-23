package au.com.library.book.kafka.event;

import org.apache.commons.lang.StringUtils;

/**
 * Represents the event types received from the Loan Service.
 *
 */
public enum LoanEventType {

    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#LOANED loaned}.    */
    LOAN_CREATED("loan-created"),
    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available} because a loan has been returned.
     */
    LOAN_RETURNED("loan-returned"),
    /**
     * The event type that signals that an edition copy needs to be marked as {@link au.com.library.book.entity.EditionCopyStatus#LOST lost}.
     */
    LOAN_MARKED_LOST("loan-marked-lost");

    /**
     * Retrieves the LoanEventType corresponding to the given string value.
     *
     * @param value The String value representing the event type.
     * @return The corresponding LoanEventType.
     * @throws IllegalArgumentException If the value is null, blank, or does not correspond to any known event type.
     */
    public static LoanEventType forValue(String value) {
        if(StringUtils.isBlank(value)){
            throw new IllegalArgumentException("The LoanEventType value cannot be null or blank");
        }
        for (LoanEventType type : LoanEventType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown LoanEventType value: " + value);
    }

    private final String value;

    private LoanEventType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
