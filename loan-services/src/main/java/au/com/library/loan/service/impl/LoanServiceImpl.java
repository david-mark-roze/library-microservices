package au.com.library.loan.service.impl;

import au.com.library.contracts.event.loan.LoanEvent;
import au.com.library.contracts.event.loan.LoanEventContext;
import au.com.library.loan.client.BookClient;
import au.com.library.loan.client.MemberClient;
import au.com.library.loan.dto.*;
import au.com.library.loan.entity.Loan;
import au.com.library.loan.entity.LoanStatus;
import au.com.library.loan.exception.CopyUnavailableException;
import au.com.library.loan.mapper.LoanMapper;
import au.com.library.loan.repository.LoanRepository;
import au.com.library.loan.service.HoldRequestService;
import au.com.library.loan.service.LoanService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Period;
import java.util.Optional;

import static au.com.library.contracts.event.loan.LoanEventType.*;

/**
 * Implementation of the {@link LoanService} interface.
 */
@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private final BookClient bookClient;
    private final MemberClient memberClient;

    private final ApplicationEventPublisher eventPublisher;
    private final HoldRequestService holdRequestService;
    private final LoanRepository loanRepository;

    private final LoanMapper mapper;

    @Value("${loan.period-days}")
    private int loanPeriodDays;

    @Value("${renewal.limit}")
    private int renewalLimit;

    /**
     * Creates a new loan for a library book edition copy.
     *
     * @param loanRequestDTO A {@link LoanRequestDTO} containing the data required to create the loan.
     * @return A {@link LoanDTO} containing details of the created loan.
     * @throws CopyUnavailableException if the requested edition copy is not available for loan.
     */
    @Override
    @Transactional
    public LoanDTO createLoan(LoanRequestDTO loanRequestDTO) throws CopyUnavailableException {
        Optional<Loan> activeLoan = loanRepository.findActiveLoanForEditionCopy(loanRequestDTO.editionCopyId());
        if(activeLoan.isPresent()){
            throw new CopyUnavailableException("The copy of the book requested is already on loan and is not currently available");
        }
        EditionCopySnapshotDTO copy = bookClient.findCopy(loanRequestDTO.editionCopyId());
        MemberSnapshotDTO member = memberClient.findMember(loanRequestDTO.memberId());
        EditionSnapshotDTO edition = bookClient.findEdition(copy.getEditionId());
        BookSnapshotDTO book = bookClient.findBook(edition.getBookId());

        holdRequestService.loanCreationHoldChecking(copy, member.getId());
        Loan loan = Loan.builder().
                editionCopyId(loanRequestDTO.editionCopyId()).
                bookTitle(book.getTitle()).
                author(book.getAuthor()).
                edition(edition.getEdition()).
                barcode(copy.getBarcode()).
                memberId(member.getId()).
                memberFirstName(member.getFirstName()).
                memberLastName(member.getLastName()).
                loanPeriod(Period.ofDays(loanPeriodDays)).
                build();
        Loan saved = saveNewLoan(loan);
        // Publish loan created event for all registered listeners.
        eventPublisher.publishEvent(loanCreatedEvent(saved));
        //return Mapper.map(saved, LoanDTO.class);
        return mapper.toDTO(saved);
    }

    /**
     * Renews an existing loan.
     * @param id The id of the loan to renew.
     * @return A {@link LoanDTO} containing details of the renewed loan.
     * @throws ConflictException Thrown if the loan is not in a state that allows it to be renewed or has exceeded the maximum number of renewals.
     * @throws ResourceNotFoundException Thrown if the loan with the specified id could not be found.
     * @throws IllegalArgumentException Thrown if the provided id is null or not a positive non-zero value.
     */
    @Override
    public LoanDTO renewLoan(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        validateId(id);
        Loan loan = findById(id);
        if(!loan.getStatus().isActive()){
            throw new ConflictException(String.format("Only loans with status %s or %s can be renewed",
                    LoanStatus.BORROWED, LoanStatus.RENEWED));
        }
        if(loan.getRenewalCount() >= renewalLimit){
            throw new ConflictException("The maximum number of renewals has been reached for this loan");
        }
        loan.renewLoan(Period.ofDays(loanPeriodDays));
        Loan renewed = loanRepository.save(loan);
        return mapper.toDTO(renewed);
    }

    /**
     * Returns a loaned book edition copy.
     *
     * @param id The id of the loan to return.
     * @return A {@link LoanDTO} containing details of the returned loan.
     * @throws ConflictException if the loan is not in a state that allows it to be returned.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    @Transactional
    public LoanDTO returnLoan(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        Loan loan = findById(id);
        loan.returnLoan();
        Loan saved = loanRepository.save(loan);
        // Check if there are any active hold requests for the edition of the returned copy and,
        // if so, allocate the returned copy to the earliest active hold request.
        EditionCopySnapshotDTO editionCopySnapshot = bookClient.findCopy(loan.getEditionCopyId());
        holdRequestService.loanReturnAllocation(editionCopySnapshot);
        // Publish loan returned event for all registered listeners.
        eventPublisher.publishEvent(loanReturnedEvent(saved));
        return mapper.toDTO(saved);
    }

    /**
     * Marks a loaned book edition copy as lost.
     *
     * @param id The id of the loan to mark as lost.
     * @return A {@link LoanDTO} containing details of the lost loan.
     * @throws ConflictException if the loan is not in a state that allows it to be marked as lost.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    @Transactional
    public LoanDTO markLost(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        Loan loan = findById(id);
        loan.markLost();
        Loan saved = loanRepository.save(loan);
        eventPublisher.publishEvent(loanLostEvent(saved));
        return mapper.toDTO(saved);
    }

    /**
     * Finds a loan by its unique id.
     *
     * @param id The id of the loan to find.
     * @return A {@link LoanDTO} containing details of the found loan.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    public LoanDTO find(Long id) throws ResourceNotFoundException, IllegalArgumentException {
        return mapper.toDTO(findById(id));
    }

    private LoanEvent loanCreatedEvent(Loan loan){
        return new LoanEvent(LOAN_CREATED,loanEventContext(loan));
    }

    private LoanEvent loanReturnedEvent(Loan loan){
        return new LoanEvent(LOAN_RETURNED, loanEventContext(loan));
    }

    private LoanEvent loanLostEvent(Loan loan){
        return new LoanEvent(LOAN_MARKED_LOST, loanEventContext(loan));
    }

    private LoanEventContext loanEventContext(Loan loan){
        return new LoanEventContext(loan.getId(), loan.getMemberId(), loan.getEditionCopyId());
    }

    private void validateId(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("The loan id must be a positive non-zero value");
        }
    }

    private Loan findById(Long id){
        validateId(id);
        return loanRepository.findById(id).
                orElseThrow(
                        ()-> new ResourceNotFoundException(String.format("The loan with the id %s could not be found", id)
                        )
                );
    }

    private Loan saveNewLoan(Loan loan) throws CopyUnavailableException {
        try {
            return loanRepository.save(loan);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            // Will occur when the edition copy is already on loan and the unique constraint on openCopyId is violated.
            LOGGER.error(e.getMessage(), e);
            throw new CopyUnavailableException("The copy of the book requested is already on loan and is not currently available");
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
