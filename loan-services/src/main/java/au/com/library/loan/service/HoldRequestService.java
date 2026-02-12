package au.com.library.loan.service;

import au.com.library.loan.dto.EditionCopySnapshotDTO;
import au.com.library.loan.dto.HoldRequestDTO;
import au.com.library.loan.entity.HoldRequestStatus;
import au.com.library.loan.exception.BlockedLoanException;
import au.com.library.loan.exception.DuplicateHoldRequestException;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ResourceNotFoundException;

/**
 * Service interface for managing hold requests in the library loan system.
 */
public interface HoldRequestService {
    /**
     * Place a loan hold request for a member on a specific edition.
     * @param memberId The ID of the member placing the hold request.
     * @param editionId The ID of the edition for which the hold request is being placed.
     * @return  A {@link HoldRequestDTO} object containing the result of the hold request placement.
     * @throws ResourceNotFoundException Thrown when the member or edition could not be found.
     * @throws DuplicateHoldRequestException Thrown when a duplicate hold request is for the same member and edition.
     */
    HoldRequestDTO placeHoldRequest(Long memberId, Long editionId) throws ResourceNotFoundException;

    /**
     * Checks for any 'open' hold requests (i.e. {@link HoldRequestStatus#ACTIVE active} or {@link HoldRequestStatus#ALLOCATED allocated})
     * for the edition associated with the provided edition copy snapshot. This is delegate to from the loan creation process to deal with any hold restrictions that may apply to the loan creation.
     * <p>If there are no open hold requests, the loan creation can proceed without hold restrictions.
     * If there is an open hold request, it checks if it belongs to the member attempting to create the loan. If it does, the loan creation can proceed but the hold request status needs to be updated accordingly.
     * If it does not belong to the member, a BlockedLoanException should be thrown to indicate that the loan cannot be created due to an active hold request for another member.</p>
     *
     * @param editionCopySnapshot the snapshot of the edition copy for which the loan is being created. The edition ID from this snapshot will be used to check for open hold requests.
     * @param memberId the ID of the member for whom the loan is being created. This will be used to check if any open hold request belongs to this member.
     * @throws BlockedLoanException Thrown if there is an open hold request for the edition that belongs to another member, indicating that the loan cannot be created due to hold restrictions,
     * such as an active hold request for another member.
     * @throws ConflictException Thrown if there is an open hold request for the edition that belongs to the member but is in an invalid state for loan creation hold checking.
     */
    void loanCreationHoldChecking(EditionCopySnapshotDTO editionCopySnapshot, Long memberId) throws BlockedLoanException, ConflictException;

    /**
     * Checks for any active hold requests (i.e. {@link HoldRequestStatus#ACTIVE active}) for the edition associated with the provided edition copy snapshot. This is delegate to from the loan return process to deal with any hold restrictions that may apply to the loan return.
     * <p>If there are no active hold requests, the loan return can proceed without hold restrictions.
     * If there is an active hold request, it will be allocated to the returned copy.</p>
     *
     * @param editionCopySnapshot the snapshot of the edition copy for which the loan is being returned. The edition ID from this snapshot will be used to check for active hold requests.
     *
     */
    void loanReturnAllocation(EditionCopySnapshotDTO editionCopySnapshot) throws ConflictException;

    /**
     * Cancels a hold request with the specified ID. The hold request status will be changed to cancelled and if there is an associated hold allocation, it will also be marked as cancelled.
     * If the hold request is completed, it cannot be cancelled and a ConflictException will be thrown. If the hold request is not found, a ResourceNotFoundException will be thrown.
     *
     * @param holdRequestId The ID of the hold request to cancel.
     * @return A {@link HoldRequestDTO} object containing the details of the cancelled hold request.
     * @throws ResourceNotFoundException Thrown when a hold request with the specified ID could not be found.
     * @throws ConflictException Thrown when the hold request is in a completed state and therefore cannot be cancelled.
     */
    HoldRequestDTO cancelHoldRequest(Long holdRequestId) throws ResourceNotFoundException, ConflictException;
}
