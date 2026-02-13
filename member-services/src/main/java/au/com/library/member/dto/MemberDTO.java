package au.com.library.member.dto;

/**
 * Data Transfer Object for Member entity. This record encapsulates the member's information that is transferred between different layers of the application.
 *
 * @param id        Unique identifier for the member.
 * @param firstName The member's first name.
 * @param lastName  The member's last name.
 * @param email     The member's email address.
 * @param phone     The member's phone number.
 * @param address1  The first line of the member's address.
 * @param address2  The second line of the member's address (optional).
 * @param city      The city where the member resides.
 * @param state     The state where the member resides.
 * @param postcode  The postal code for the member's address.
 */
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
