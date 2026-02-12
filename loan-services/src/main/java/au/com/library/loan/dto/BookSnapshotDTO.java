package au.com.library.loan.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookSnapshotDTO(
        String title,
        String author
) {
}
