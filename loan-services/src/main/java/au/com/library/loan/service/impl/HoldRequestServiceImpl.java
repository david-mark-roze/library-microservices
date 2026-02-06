package au.com.library.loan.service.impl;

import au.com.library.loan.client.BookClient;
import au.com.library.loan.client.MemberClient;
import au.com.library.loan.dto.*;
import au.com.library.loan.entity.HoldAllocation;
import au.com.library.loan.entity.HoldRequest;
import au.com.library.loan.entity.HoldRequestStatus;
import au.com.library.loan.exception.BlockedLoanException;
import au.com.library.loan.exception.DuplicateHoldRequestException;
import au.com.library.loan.mapper.HoldRequestMapper;
import au.com.library.loan.repository.HoldAllocationRepository;
import au.com.library.loan.repository.HoldRequestRepository;
import au.com.library.loan.service.HoldRequestService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Implementation of the {@link HoldRequestService} interface.
 */
@RequiredArgsConstructor
@Service
public class HoldRequestServiceImpl implements HoldRequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoldRequestServiceImpl.class);

    private final BookClient bookClient;
    private final MemberClient memberClient;

    private final HoldRequestRepository holdRequestRepository;
    private final HoldAllocationRepository allocationRepository;

    private final HoldRequestMapper mapper;

    @Value("${loan.hold-period-days}")
    private int loanHoldPeriodDays;

    @Override
    public HoldRequestResultDTO placeHoldRequest(Long memberId, Long editionId) throws ResourceNotFoundException {
        validateId(memberId, "Member ID");
        validateId(editionId, "Edition ID");

        EditionSnapshotDTO editionSnapshot = bookClient.findEdition(editionId);
        BookSnapshotDTO bookSnapshot = bookClient.findBook(editionSnapshot.getBookId());
        MemberSnapshotDTO memberSnapshot = memberClient.findMember(memberId);
        HoldRequest holdRequest = HoldRequest.builder()
                .editionId(editionSnapshot.getId())
                .edition(editionSnapshot.getEdition())
                .bookTitle(bookSnapshot.getTitle())
                .author(bookSnapshot.getAuthor())
                .memberId(memberSnapshot.getId())
                .memberFirstName(memberSnapshot.getFirstName())
                .memberLastName(memberSnapshot.getLastName())
                .email(memberSnapshot.getEmail())
                .phone(memberSnapshot.getPhone())
                .build();
        try {
            HoldRequest savedHoldRequest = holdRequestRepository.save(holdRequest);
            return mapper.toDTO(savedHoldRequest);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DuplicateHoldRequestException(String.format("A hold request for member ID %d on edition ID %d already exists.", memberId, editionId));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void loanCreationHoldChecking(EditionCopySnapshotDTO editionCopySnapshot, Long memberId) throws BlockedLoanException {
        HoldRequest holdRequest = findOpenHoldRequests(editionCopySnapshot.getEditionId());
        // If there are no open hold requests for the edition, the loan creation can proceed without hold restrictions
        if(holdRequest == null){
            LOGGER.info("No open hold requests found for edition ID {0}. Loan creation can proceed without hold restrictions.", editionCopySnapshot.getEditionId());
            return;
        }
        // If there is an open hold request for the edition, check if it belongs to the member attempting to create the loan. If it does, the loan creation can proceed but the hold request status needs to be updated accordingly.
        // If it does not belong to the member, a BlockedLoanException should be thrown to indicate that the loan cannot be created due to an active hold request for another member.
        if (holdRequest.getMemberId().equals(memberId)) {
            HoldRequestStatus status = holdRequest.getStatus();
            switch (status){
                case ACTIVE -> allocateHoldRequest(holdRequest, editionCopySnapshot);
                case ALLOCATED -> handleAllocatedHoldRequest(holdRequest, editionCopySnapshot, memberId);
                default -> throw new ConflictException("The hold request for this member is in an invalid state for loan creation hold checking: " + status);
            }
        } else {
            throw new BlockedLoanException("This edition is currently on hold for another member.");
        }
    }

    @Override
    public void loanReturnAllocation(EditionCopySnapshotDTO editionCopySnapshot) {
        HoldRequest holdRequest = findActiveHoldRequest(editionCopySnapshot.getEditionId());
        if (holdRequest != null) {
            allocateHoldRequest(holdRequest, editionCopySnapshot);
        }
    }

    private void validateId(Long id, String label) throws IllegalArgumentException {
        if(id == null || id == 0){
            throw new IllegalArgumentException(String.format("%s must be provided and greater than zero.", label));
        }
    }

    private HoldRequest findOpenHoldRequests(Long editionId) {
        List<HoldRequest> holdRequests = holdRequestRepository.lockedOpenHoldRequestsByEditionId(editionId, PageRequest.of(0, 1));
        if (!holdRequests.isEmpty()) {
            return holdRequests.get(0);
        }
        return null;
    }

    private HoldRequest findActiveHoldRequest(Long editionId) {
        List<HoldRequest> holdRequests = holdRequestRepository.lockedActiveHeadHoldRequest(editionId, PageRequest.of(0, 1));
        if (!holdRequests.isEmpty()) {
            return holdRequests.get(0);
        }
        return null;
    }

    private void allocateHoldRequest(HoldRequest holdRequest, EditionCopySnapshotDTO editionCopySnapshot) throws BlockedLoanException {
        if(holdRequest.hasAllocation()){
            throw new ConflictException("The hold request already has an allocation and cannot be allocated again.");
        }
        holdRequest.markAsAllocated();
        var allocation = HoldAllocation.builder()
                .holdRequest(holdRequest)
                .editionCopyId(editionCopySnapshot.getId())
                .barcode(editionCopySnapshot.getBarcode())
                .allocationDuration(Duration.of(loanHoldPeriodDays, ChronoUnit.DAYS))
                .build();
        holdRequestRepository.save(holdRequest);
        allocationRepository.save(allocation);
        LOGGER.info("Hold request with ID {0} has been marked as allocated and a new hold allocation has been created for edition ID {1}.", holdRequest.getId(), editionCopySnapshot.getEditionId());
    }

    private void handleAllocatedHoldRequest(HoldRequest holdRequest, EditionCopySnapshotDTO editionCopySnapshot, Long memberId) throws BlockedLoanException {
        holdRequest.markAsCompleted();
        var allocation = holdRequest.getAllocation();
        allocation.markAsCollected();
        holdRequestRepository.save(holdRequest);
        allocationRepository.save(allocation);
        LOGGER.info("Hold request with ID {0} has been marked as completed and the associated hold allocation has been marked as collected for member ID {1} and edition ID {2}.", holdRequest.getId(), memberId, editionCopySnapshot.getEditionId());
    }
}
