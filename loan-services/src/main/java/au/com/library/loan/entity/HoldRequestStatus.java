package au.com.library.loan.entity;

/**
 * Enumeration representing the status of a hold request for the loan of an edition copy.
 */
public enum HoldRequestStatus {
    /**
     * The hold request is currently active and awaiting allocation of an edition copy for loaning.
     */
    ACTIVE,
    /**
     * The hold request has been allocated an edition copy for loaning.
     */
    ALLOCATED,
    /**
     * The hold request has expired i.e. the member did not collect the edition copy within the allocated time frame.
     */
    EXPIRED,
    /**
     * The hold request has been completed i.e. the member has collected the edition copy for loaning.
     */
    COMPLETED,
    /**
     * The hold request has been cancelled.
     */
    CANCELLED;

    /**
     * Indicates that the hold request status is {@link #ACTIVE active}
     * i.e. the hold request is currently active and awaiting allocation of an edition copy for loaning.
     *
     * @return true if the status is {@link #ACTIVE active}, false otherwise.
     */
    public boolean isActive() {
        return ACTIVE.equals(this);
    }

    /**
     * Indicates that a hold request has an allocated edition copy for loaning.
     *
     * @return true if the status is {@link #ALLOCATED allocated}, false otherwise.
     */
    public boolean isAllocated() {
        return ALLOCATED.equals(this);
    }

    /**
     * Indicates that the hold request has expired i.e. the member did not collect the edition copy within the allocated time frame.
     *
     * @return true if the status is {@link #EXPIRED expired}, false otherwise.
     */
    public boolean isExpired() {
        return EXPIRED.equals(this);
    }

    /**
     * Indicates that a hold request has been completed i.e. the member has collected the edition copy for loaning.
     *
     * @return true if the status is {@link #COMPLETED completed}, false otherwise.
     */
    public boolean isCompleted() {
        return COMPLETED.equals(this);
    }

    /**
     * Indicates that a hold request has been cancelled.
     *
     * @return true if the status is {@link #CANCELLED cancelled}, false otherwise.
     */
    public boolean isCancelled() { return CANCELLED.equals(this); }
}
