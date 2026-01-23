package au.com.library.book.kafka.event;

/**
 * Represents the context of a loan event from the book services consumer perspective,
 * encapsulating identifiers for the loan, member, and edition copy involved in the event.
 *
 * @param loanId The unique identifier of the loan.
 * @param memberId The unique identifier of the member associated with the loan.
 * @param editionCopyId The unique identifier of the edition copy involved in the loan event.
 */
public record LoanEventContext(
        Long loanId,
        Long memberId,
        Long editionCopyId
) {}
