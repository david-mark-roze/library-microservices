package au.com.library.member.dto;

public record MemberDTO(
         Long id,
         String firstName,
         String lastName,
         String email,
         String phone,
         String address1,
         String address2,
         String city,
         String state,
         String postcode
) {
}
