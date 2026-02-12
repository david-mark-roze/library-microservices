package au.com.library.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EditionCopySnapshotDTO(
         Long id,
         String barcode,
         EditionCopyStatus status,
         Long editionId) {
}
