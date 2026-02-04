package au.com.library.loan.dto;

import au.com.library.loan.entity.HoldRequestStatus;

import java.time.LocalDate;

public record HoldRequestResultDTO(
        Long id,
        Long memberId,
        String memberFirstName,
        String memberLastName,
        String email,
        String phone,
        Long editionId,
        String edition,
        String bookTitle,
        String author,
        HoldRequestStatus status,
        LocalDate dateRequested
) {
}
