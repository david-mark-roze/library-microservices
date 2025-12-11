package au.com.library.member.service;

import au.com.library.member.dto.MemberDTO;
import au.com.library.member.exception.DuplicateEmailAddressException;
import au.com.library.shared.exception.ResourceNotFoundException;

/**
 * Handles service layer operations for library members.
 */
public interface MemberService {

    /**
     * Handles the creation of a new {@link au.com.library.member.entity.Member library member}.
     * @param memberDTO A {@link MemberDTO} object containing the member details to create.
     * @return A {@link MemberDTO} object containing the details of the newly created member.
     * @throws DuplicateEmailAddressException Thrown when the email address specified is already being used by another member.
     */
    MemberDTO add(MemberDTO memberDTO) throws DuplicateEmailAddressException;

    /**
     * Finds the library member details by its unique id and returns the details as a {@link MemberDTO} object.
     * @param id The library member id.
     * @return A {@link MemberDTO} object containing the fetched details.
     * @throws ResourceNotFoundException Thrown when a member with the provided id could not be found.
     */
    MemberDTO find(Long id) throws ResourceNotFoundException;

    /**
     * Handles the update of an existing library member's details.
     * @param id The library id/number of the member.
     * @param memberDTO A {@link MemberDTO} object containing the member details to update.
     * @return A {@link MemberDTO} object containing the details of the member post update.
     * @throws ResourceNotFoundException Thrown when the member to update could not be found with the specified id.
     */
    MemberDTO update(Long id, MemberDTO memberDTO) throws ResourceNotFoundException;
}
