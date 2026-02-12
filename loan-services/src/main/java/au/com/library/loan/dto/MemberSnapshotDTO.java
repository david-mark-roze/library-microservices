package au.com.library.loan.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MemberSnapshotDTO(
         Long id,
         String firstName,
         String lastName,
         String email,
         String phone
        ) {
}
