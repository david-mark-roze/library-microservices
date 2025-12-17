package au.com.library.book.entity;

/**
 * Represents a possible status of a {@link EditionCopy copy} of a {@link Book library book}
 * indicating its availability.
 */
public enum EditionCopyStatus {

    AVAILABLE,
    LOANED,
    LOST;

    public boolean isLoaned(){
        return LOANED.equals(this);
    }

    public boolean isAvailable(){
        return AVAILABLE.equals(this);
    }

    public boolean isLost(){
        return LOST.equals(this);
    }
}
