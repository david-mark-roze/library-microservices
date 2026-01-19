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

    @Column(nullable = false)
    private LocalDate loanDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Setter
    private int renewalCount;

    @Setter
    private LocalDate returnDate;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @Builder
    public Loan(Long id, Long memberId, String memberFirstName, String memberLastName, Long editionCopyId, String bookTitle, String author, String edition, String barcode, LocalDate loanDate, LocalDate returnDate, LoanStatus status) {
        this.id = id;
        this.memberId = memberId;
        this.memberFirstName = memberFirstName;
        this.memberLastName = memberLastName;
        this.editionCopyId = editionCopyId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.edition = edition;
        this.barcode = barcode;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    /**
     * Calculates the {@link #getDueDate() due date} of based on the specified relative number of days from the loan date.
     * @param loanPeriodDays The number of days from the loan date to calculate the due date.
     * @throws IllegalArgumentException Thrown if the specified loan period days is less than or equal to zero.
     * @throws IllegalStateException Thrown if no loan date has been set or the due date cannot be calculated.
     */
    public void calculateDueDate(int loanPeriodDays){
        if(loanPeriodDays <= 0){
            throw new IllegalArgumentException("The loan period days must be greater than zero");
        }
        if(loanDate == null){
            throw new IllegalStateException("The loan date has not been set. Unable to calculate due date");
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
     * Handles the renewal of a loan. Only {@link LoanStatus#BORROWED borrowed} or {@link LoanStatus#RENEWED renewed} loans may be renewed. The {@link #getDueDate() due date} will be extended by the specified number of days.
     *
     * @param loanPeriodDays The number of days to extend the due date by.
     * @throws ConflictException Thrown when the loan is not in a state that allows renewal.
     * @throws IllegalArgumentException Thrown when the specified loan period days is less than or equal to zero.
     */
    public void renewLoan(int loanPeriodDays){
        if(loanPeriodDays <= 0){
            throw new IllegalArgumentException("The loan period days must be greater than zero");
        }
        // Only borrowed or renewed loans may be renewed.
        if (isRenewable()) {
            handleRenewal(loanPeriodDays);
        } else {
            throw new ConflictException(String.format("The loan with id %s cannot be renewed. Its status is %s", id, status));
        }
    }

    /**
     * Determines if this loan is in a state that allows renewal.
     * @return true if the loan is {@link LoanStatus#BORROWED borrowed} or {@link LoanStatus#RENEWED renewed}; false otherwise.
     */
    public boolean isRenewable(){
        return (status.isBorrowed() || status.isRenewed());
    }

    /**
     * Determines if this loan is overdue.
     * @return true if the current date is past the {@link #getDueDate() due date} and the loan is {@link LoanStatus#BORROWED borrowed} or {@link LoanStatus#RENEWED renewed}; false otherwise.
     */
    public boolean isOverdue(){
        return dueDate.isBefore(LocalDate.now()) && (status.isBorrowed() || status.isRenewed());
    }

    private void handleRenewal(int loanPeriodDays){
        dueDate = dueDate.plusDays(loanPeriodDays);
        if (status.isBorrowed()) {
            status = LoanStatus.RENEWED;
        }
        renewalCount++;
    }
}
