package au.com.library.book.entity;

/**
 * Represents a possible status of a {@link EditionCopy copy} of a {@link Book library book}
 * indicating its availability.
 */
public enum EditionCopyStatus {

    AVAILABLE,
    LOANED,
    LOST
}
