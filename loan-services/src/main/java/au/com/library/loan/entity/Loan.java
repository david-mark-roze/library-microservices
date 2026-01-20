package au.com.library.loan.entity;

import au.com.library.shared.exception.ConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private String memberFirstName;

    @Column(nullable = false)
    private String memberLastName;

    @Column(nullable = false)
    private Long editionCopyId;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String edition;

    @Column(nullable = false)
    private String barcode;

    /**
     * Set to the current date when the loan is created.
     */
    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * Number of times the loan has been renewed. Will have an initial value of 0.
     */
    @Column(nullable = false)
    private int renewalCount;

    private LocalDate returnDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Builder
    /**
     * Constructs a new Loan entity with the specified parameters.
     * @param id The unique identifier of the loan.
     * @param memberId The unique identifier of the member associated with the loan.
     * @param memberFirstName The first name of the member.
     * @param memberLastName The last name of the member.
     * @param editionCopyId The unique identifier of the edition copy being loaned.
     * @param bookTitle The title of the book being loaned.
     * @param author The author of the book being loaned.
     * @param edition The edition of the book being loaned.
     * @param barcode The barcode of the edition copy being loaned.
     * @param returnDate The date when the loan was returned.
     *
     */
    public Loan(Long memberId, String memberFirstName, String memberLastName, Long editionCopyId, String bookTitle, String author, String edition, String barcode) {
        this.memberId = memberId;
        this.memberFirstName = memberFirstName;
        this.memberLastName = memberLastName;
        this.editionCopyId = editionCopyId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.edition = edition;
        this.barcode = barcode;
        this.status = LoanStatus.BORROWED;
        this.loanDate = LocalDate.now();
        this.renewalCount = 0;
    }

    /**
     * Calculates the {@link #getDueDate() due date} of based on the specified relative number of days from the loan date.
     * @param loanPeriodDays The number of days from the loan date to calculate the due date.
     * @throws IllegalArgumentException Thrown if the specified loan period days is less than or equal to zero.
     */
    public void calculateDueDate(int loanPeriodDays){
        if(loanPeriodDays <= 0){
            throw new IllegalArgumentException("The loan period days must be greater than zero");
        }
        dueDate = loanDate.plusDays(loanPeriodDays);
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
    }

    /**
     * Handles the renewal of a loan. This includes {@link LoanStatus#BORROWED borrowed} and {@link LoanStatus#RENEWED renewed} loans,
     * where, with the latter, the loan may be extended while still under renewal.
     * The {@link #getDueDate() due date} will be extended by the specified number of days.
     *
     * @param loanPeriodDays The number of days to extend the due date by.
     * @throws ConflictException Thrown when the loan is not in a state that allows renewal.
     * @throws IllegalArgumentException Thrown when the specified loan period days is less than or equal to zero.
     */
    public void renewLoan(int loanPeriodDays){
        if(loanPeriodDays <= 0){
            throw new IllegalArgumentException("The loan period days must be greater than zero");
        }
        if (getStatus().isActive()) {
            handleRenewal(loanPeriodDays);
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
    }

    private void handleRenewal(int loanPeriodDays){
        dueDate = dueDate.plusDays(loanPeriodDays);
        if (status.isBorrowed()) {
            status = LoanStatus.RENEWED;
        }
        renewalCount++;
    }
}
