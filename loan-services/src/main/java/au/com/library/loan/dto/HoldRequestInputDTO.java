package au.com.library.loan.dto;

public record HoldRequestInputDTO(

        Long memberId,
        Long editionId
) {
}
