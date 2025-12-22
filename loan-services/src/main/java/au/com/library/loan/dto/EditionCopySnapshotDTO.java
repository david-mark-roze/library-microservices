package au.com.library.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditionCopySnapshotDTO {

    private String barcode;
    private EditionCopyStatus status;
    private Long editionId;
}
