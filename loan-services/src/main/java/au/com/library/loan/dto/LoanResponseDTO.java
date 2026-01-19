package au.com.library.loan.dto;

import au.com.library.loan.entity.LoanStatus;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanResponseDTO {

    private Long id;
    private Long memberId;
    private String memberFirstName;
    private String memberLastName;
    private Long editionCopyId;
    private String bookTitle;
    private String author;
    private String barcode;
    private String edition;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LoanStatus status;
    private int renewalCount;
}
