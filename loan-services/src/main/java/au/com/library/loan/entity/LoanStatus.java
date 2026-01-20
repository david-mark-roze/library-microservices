package au.com.library.loan.entity;

public enum LoanStatus {

    BORROWED, RETURNED, RENEWED, LOST;

    /**
     * Indicates that the loan status is BORROWED.
     *
     * @return true if the status is BORROWED, false otherwise.
     */
    public boolean isBorrowed(){
        return BORROWED.equals(this);
    }

    /**
     * Indicates that the loan status is RETURNED.
     *
     * @return true if the status is RETURNED, false otherwise.
     */
    public boolean isReturned(){
        return RETURNED.equals(this);
    }

    /**
     * Indicates that the loan status is LOST.
     *
     * @return true if the status is LOST, false otherwise.
     */
    public boolean isLost(){
        return LOST.equals(this);
    }
    /**
     * Indicates that the loan status is RENEWED.
     *
     * @return true if the status is RENEWED, false otherwise.
     */
    public boolean isRenewed(){
        return RENEWED.equals(this);
    }

    /**
     * Indicates that the loan is currently active (either BORROWED or RENEWED).
     *
     * @return true if the loan is active, false otherwise.
     */
    public boolean isActive(){
        return isBorrowed() || isRenewed();
    }
}
