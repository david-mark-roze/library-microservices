package au.com.library.loan.entity;

public enum HoldAllocationStatus {
    /**
     * The loan hold has been allocated to a member i.e. the edition copy is set aside for them for when they come to collect it.
     */
    ALLOCATED,
    /**
     * The member has collected the edition copy for loaning.
     */
    COLLECTED,
    /**
     * The loan hold has expired i.e. the member did not collect the edition copy within the allocated time frame.
     */
    EXPIRED,
    /**
     * The loan hold has been cancelled i.e. the member has chosen to cancel their hold request.
     */
    CANCELLED;

    /**
     * Indicates that the hold allocation status is {@link #ALLOCATED allocated}.
     *
     * @return true if the status is {@link #ALLOCATED allocated}, false otherwise.
     */
    public boolean isAllocated(){
        return ALLOCATED.equals(this);
    }

    /**
     * Indicates that the hold allocation status is {@link #COLLECTED collected}.
     *
     * @return true if the status is {@link #COLLECTED collected}, false otherwise.
     */
    public boolean isCollected(){
        return COLLECTED.equals(this);
    }

    /**
     * Indicates that the hold allocation status is {@link #EXPIRED expired}.
     *
     * @return true if the status is {@link #EXPIRED expired}, false otherwise.
     */
    public boolean isExpired(){
        return EXPIRED.equals(this);
    }

    /**
     * Indicates that the hold allocation status is {@link #CANCELLED cancelled}.
     *
     * @return true if the status is {@link #CANCELLED cancelled}, false otherwise.
     */
    public boolean isCancelled(){
        return CANCELLED.equals(this);
    }
}
