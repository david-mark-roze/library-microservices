package au.com.library.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EditionSnapshotDTO(
         Long id,
         String edition,
         Long bookId
) {
}
