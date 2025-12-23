package au.com.library.loan.entity;

import au.com.library.shared.exception.ConflictException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
    private LocalDate returnDate;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    /**
     * Calculates the {@link #getDueDate() due date} of based on the specified relative number of days from the loan date.
     * @param loanPeriodDays The number of days from the loan date to calculate the due date.
     * @throws IllegalStateException Thrown if
     */
    public void calculateDueDate(int loanPeriodDays){
        if(loanDate == null){
            throw new IllegalStateException("The due date has not been set");
        }
        dueDate = loanDate.plusDays(loanPeriodDays);
    }

    /**
     * Marks a loan as {@link LoanStatus#RETURNED returned} if it is currently
     * {@link LoanStatus#BORROWED borrowed}, {@link LoanStatus#RENEWED renewed} or
     * {@link LoanStatus#OVERDUE overdue} and sets the {@link #getReturnDate() return date}.
     * @throws ConflictException Thrown when the loan has already been returned.
     * @throws IllegalStateException Thrown when the loan is {@link LoanStatus#LOST}.
     */
    public void returnLoan(){
        if(status.isLost()){
            throw new IllegalStateException("Unable to mark the loan as returned. Its status is lost");
        }
        if(status.isReturned()){
            throw new ConflictException(String.format("The loan with id %s has already been returned", id));
        }
        status = LoanStatus.RETURNED;
        returnDate = LocalDate.now();
    }
}
