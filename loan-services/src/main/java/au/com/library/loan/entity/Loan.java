package au.com.library.loan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
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
}
