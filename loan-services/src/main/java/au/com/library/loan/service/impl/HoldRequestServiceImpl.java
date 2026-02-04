package au.com.library.loan.service.impl;

import au.com.library.loan.client.BookClient;
import au.com.library.loan.client.MemberClient;
import au.com.library.loan.dto.BookSnapshotDTO;
import au.com.library.loan.dto.EditionSnapshotDTO;
import au.com.library.loan.dto.HoldRequestResultDTO;
import au.com.library.loan.dto.MemberSnapshotDTO;
import au.com.library.loan.entity.HoldRequest;
import au.com.library.loan.exception.DuplicateHoldRequestException;
import au.com.library.loan.mapper.HoldRequestMapper;
import au.com.library.loan.repository.HoldRequestRepository;
import au.com.library.loan.service.HoldRequestService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
    private final HoldRequestMapper mapper;

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

    private void validateId(Long id, String label) throws IllegalArgumentException {
        if(id == null || id == 0){
            throw new IllegalArgumentException(String.format("%s must be provided and greater than zero.", label));
        }
    }
}
