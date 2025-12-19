package au.com.library.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoanRequestDTO {

    private Long editionId;
    private Long editionCopyId;
    private Long memberId;
}
