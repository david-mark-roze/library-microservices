package au.com.library.loan.service;

import au.com.library.loan.dto.HoldRequestResultDTO;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.loan.exception.DuplicateHoldRequestException;

/**
 * Service interface for managing hold requests in the library loan system.
 */
public interface HoldRequestService {
    /**
     * Place a loan hold request for a member on a specific edition.
     * @param memberId The ID of the member placing the hold request.
     * @param editionId The ID of the edition for which the hold request is being placed.
     * @return  A {@link HoldRequestResultDTO} object containing the result of the hold request placement.
     * @throws ResourceNotFoundException Thrown when the member or edition could not be found.
     * @throws DuplicateHoldRequestException Thrown when a duplicate hold request is for the same member and edition.
     */
    HoldRequestResultDTO placeHoldRequest(Long memberId, Long editionId) throws ResourceNotFoundException;
}
