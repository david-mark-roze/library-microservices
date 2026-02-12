package au.com.library.loan.dto;

public record LoanRequestDTO(

        Long editionCopyId,
        Long memberId
) {
}
