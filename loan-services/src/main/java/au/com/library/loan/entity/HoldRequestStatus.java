package au.com.library.loan.entity;

/**
 * Enumeration representing the status of a hold request for the loan of an edition copy.
 */
public enum HoldRequestStatus {
    /**
     * The hold request is currently active.
     */
    ACTIVE,
    /**
     * The hold request has been fulfilled.
     */
    FULFILLED,
    /**
     * The hold request has been cancelled.
     */
    CANCELLED;

    /**
     * Indicates that the hold request status is ACTIVE.
     *
     * @return true if the status is ACTIVE, false otherwise.
     */
    public boolean isActive() {
        return ACTIVE.equals(this);
    }
    /**
     * Indicates that the hold request status is FULFILLED.
     *
     * @return true if the status is FULFILLED, false otherwise.
     */
    public boolean isFulfilled() {
        return FULFILLED.equals(this);
    }

    /**
     * Indicates that the hold request status is CANCELLED.
     *
     * @return true if the status is CANCELLED, false otherwise.
     */
    public boolean isCancelled() {
        return CANCELLED.equals(this);
    }

}
