package au.com.library.book.entity;

/**
 * Represents a possible status of a {@link EditionCopy copy} of a {@link Book library book}
 * indicating its availability.
 */
public enum EditionCopyStatus {

    /**
     * Indicates that the edition copy is available for loan.
     */
    AVAILABLE,
    /**
     * Indicates that the edition copy is currently loaned out.
     */
    LOANED,
    /**
     * Indicates that the edition copy has been reported as lost.
     */
    LOST;

    /**
     * A convenience method that indicates if the status has been {@link #LOANED loaned}.
     *
     * @return true if the status is {@link #LOANED loaned}, false otherwise.
     */
    public boolean isLoaned(){
        return LOANED.equals(this);
    }

    /**
     * A convenience method that indicates if the status is {@link #AVAILABLE available}.
     *
     * @return true if the status is {@link #AVAILABLE available}, false otherwise.
     */
    public boolean isAvailable(){
        return AVAILABLE.equals(this);
    }

    /**
     * A convenience method that indicates if the status is {@link #LOST lost}.
     *
     * @return true if the status is {@link #LOST lost}, false otherwise.
     */
    public boolean isLost(){
        return LOST.equals(this);
    }
}
