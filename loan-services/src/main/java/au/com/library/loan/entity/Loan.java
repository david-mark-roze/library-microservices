package au.com.library.loan.entity;

import au.com.library.loan.util.ValidationUtil;
import au.com.library.shared.exception.ConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Loan {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private Long memberId;

    @Getter
    @Column(nullable = false)
    private String memberFirstName;

    @Getter
    @Column(nullable = false)
    private String memberLastName;

    @Getter
    @Column(nullable = false)
    private Long editionCopyId;

    @Getter
    @Column(nullable = false)
    private String bookTitle;

    @Getter
    @Column(nullable = false)
    private String author;

    @Getter
    @Column(nullable = false)
    private String edition;

    @Getter
    @Column(nullable = false)
    private String barcode;

    /**
     * Set to the current date when the loan is created.
     */
    @Getter
    @Column(nullable = false)
    private LocalDate loanDate;

    @Getter
    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * Number of times the loan has been renewed. Will have an initial value of 0.
     */
    @Getter
    @Column(nullable = false)
    private int renewalCount;

    @Getter
    private LocalDate returnDate;

    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    /**
     * Holds the id of the edition copy while the loan is active (borrowed or renewed). Once the loan is closed (returned or lost), this value is set to null.
     * This enforces the constraint that an edition copy can only be associated with one active loan at a time.
     */
    private Long openCopyId;

    public static final class Builder {

        private Clock clock = Clock.systemDefaultZone();

        private Long memberId;
        private String memberFirstName;
        private String memberLastName;
        private Long editionCopyId;
        private String bookTitle;
        private String author;
        private String edition;
        private String barcode;
        private Duration loanDuration;

        public Builder clock(Clock clock) {
            this.clock = ValidationUtil.checkNonNullParameter(clock, "Clock must not be null");
            return this;
        }

        public Builder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder memberFirstName(String memberFirstName) {
            this.memberFirstName = memberFirstName;
            return this;
        }

        public Builder memberLastName(String memberLastName) {
            this.memberLastName = memberLastName;
            return this;
        }

        public Builder editionCopyId(Long editionCopyId) {
            this.editionCopyId = editionCopyId;
            return this;
        }

        public Builder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder edition(String edition) {
            this.edition = edition;
            return this;
        }

        public Builder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public Builder loanDuration(Duration loanDuration) {
            this.loanDuration = loanDuration;
            return this;
        }

        public Loan build() {
            return new Loan(memberId, memberFirstName, memberLastName, editionCopyId, bookTitle, author, edition, barcode, LocalDate.now(clock), loanDuration);
        }
    }
    public static Builder builder() {
        return new Builder();
    }
    /**
     * Constructs a new Loan entity with the specified parameters.
     * @param memberId The unique identifier of the member associated with the loan.
     * @param memberFirstName The first name of the member.
     * @param memberLastName The last name of the member.
     * @param editionCopyId The unique identifier of the edition copy being loaned.
     * @param bookTitle The title of the book being loaned.
     * @param author The author of the book being loaned.
     * @param edition The edition of the book being loaned.
     * @param barcode The barcode of the edition copy being loaned.
     *
     */
    private Loan(Long memberId, String memberFirstName, String memberLastName, Long editionCopyId, String bookTitle, String author, String edition, String barcode, LocalDate loanDate, Duration loanDuration) {
        this.memberId = (Long)ValidationUtil.checkNonNullPositiveNumber(memberId, "Member ID must be a non null positive number");
        this.memberFirstName = ValidationUtil.checkNonNullParameter(memberFirstName, "Member first name must not be null");
        this.memberLastName = ValidationUtil.checkNonNullParameter(memberLastName, "Member last name must not be null");
        this.editionCopyId = (Long)ValidationUtil.checkNonNullPositiveNumber(editionCopyId, "Edition copy ID must be a non null positive number");
        this.bookTitle = ValidationUtil.checkNonNullParameter(bookTitle, "Book title must not be null");
        this.author = ValidationUtil.checkNonNullParameter(author, "Author must not be null");
        this.edition = ValidationUtil.checkNonNullParameter(edition, "Edition must not be null");
        this.barcode = ValidationUtil.checkNonNullParameter(barcode, "Barcode must not be null");
        this.status = LoanStatus.BORROWED;
        this.loanDate = ValidationUtil.checkNonNullParameter(loanDate, "Loan date must not be null");
        this.renewalCount = 0;
        this.openCopyId = editionCopyId;
        ValidationUtil.checkNonNullParameter(loanDuration, "Loan Duration must not be null");
        dueDate = loanDate.plus(loanDuration);

    }

    /**
     * Marks a loan as {@link LoanStatus#RETURNED returned} if it is currently
     * {@link LoanStatus#BORROWED borrowed}, {@link LoanStatus#RENEWED renewed} and sets the {@link #getReturnDate() return date}.
     * @throws ConflictException Thrown when the loan has already been returned.
     */
    public void returnLoan(){
        if(status.isLost()){
            throw new ConflictException("Unable to mark the loan as returned. Its status is lost");
        }
        if(status.isReturned()){
            throw new ConflictException(String.format("The loan with id %s has already been returned", id));
        }
        status = LoanStatus.RETURNED;
        returnDate = LocalDate.now();
        closeLoanCopy();
    }

    /**
     * Handles the renewal of a loan. This includes {@link LoanStatus#BORROWED borrowed} and {@link LoanStatus#RENEWED renewed} loans, where, with the latter, the loan may be extended while still under renewal.
     * The {@link #getDueDate() due date} will be extended by the specified {@link Duration}.
     *
     * @param loanDuration The duration to extend the due date by. Must not be null.
     * @throws ConflictException        Thrown when the loan is not in a state that allows renewal.
     * @throws IllegalArgumentException Thrown when the specified loan duration null or has a negative duration.
     */
    public void renewLoan(Duration loanDuration){
        ValidationUtil.checkNonNullParameter(loanDuration, "Loan duration must not be null");
        if(loanDuration.isNegative()){
            throw new IllegalArgumentException("The loan Duration must be greater than zero");
        }
        if (getStatus().isActive()) {
            dueDate = dueDate.plus(loanDuration);
            if (status.isBorrowed()) {
                status = LoanStatus.RENEWED;
            }
            renewalCount++;
        } else {
            throw new ConflictException(String.format("The loan with id %s cannot be renewed. Its status is %s", id, status));
        }
    }

    /**
     * Determines if this loan is overdue.
     *
     * @return true if the current date is past the {@link #getDueDate() due date} and the loan is {@link LoanStatus#isActive() active}; false otherwise.
     */
    public boolean isOverdue(){
        return dueDate.isBefore(LocalDate.now()) && (status.isActive());
    }

    /**
     * Marks a loan as {@link LoanStatus#LOST lost} when it has been determined to be overdue for a significant period.
     *
     * @throws ConflictException Thrown when the loan is already marked as lost or is not overdue.
     */
    public void markLost() {
        if(getStatus().isLost()){
            throw new ConflictException("The loan has already been marked as lost");
        }
        if(!isOverdue()){
            throw new ConflictException("Only overdue loans  can be marked as lost");
        }
        this.status = LoanStatus.LOST;
        closeLoanCopy();
    }

    private void closeLoanCopy() {
        this.openCopyId = null;
    }
}
