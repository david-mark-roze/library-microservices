package au.com.library.loan.dto;

import au.com.library.loan.entity.LoanStatus;

import java.time.LocalDate;

public record LoanDTO(
        Long id,
        Long memberId,
        String memberFirstName,
        String memberLastName,
        Long editionCopyId,
        String bookTitle,
        String author,
        String barcode,
        String edition,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        LoanStatus status,
        int renewalCount) {
}
