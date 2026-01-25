package au.com.library.loan.service.impl;

import au.com.library.loan.client.BookClient;
import au.com.library.loan.client.MemberClient;
import au.com.library.loan.dto.*;
import au.com.library.loan.entity.Loan;
import au.com.library.loan.entity.LoanStatus;
import au.com.library.loan.exception.CopyUnavailableException;
import au.com.library.loan.kafka.event.*;
import au.com.library.loan.repository.LoanRepository;
import au.com.library.loan.service.LoanService;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static au.com.library.loan.kafka.event.LoanEventType.*;

/**
 * The {@link LoanService} implementation.
 */
@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final BookClient bookClient;
    private final MemberClient memberClient;

    private final ApplicationEventPublisher eventPublisher;

    private final LoanRepository repository;

    @Value("${loan.period-days}")
    private int loanPeriodDays;

    @Value("${renewal.limit}")
    private int renewalLimit;

    /**
     * Creates a new loan for a library book edition copy.
     *
     * @param loanRequestDTO A {@link LoanRequestDTO} containing the data required to create the loan.
     * @return A {@link LoanResponseDTO} containing details of the created loan.
     * @throws CopyUnavailableException if the requested edition copy is not available for loan.
     */
    @Override
    @Transactional
    public LoanResponseDTO createLoan(LoanRequestDTO loanRequestDTO) throws CopyUnavailableException {

        EditionCopySnapshotDTO copy = bookClient.findCopy(loanRequestDTO.getEditionCopyId());
        if(!copy.getStatus().isAvailable()) {
            throw new CopyUnavailableException("The copy of the book requested is unavailable");
        }
        MemberSnapshotDTO member = memberClient.findMember(loanRequestDTO.getMemberId());
        EditionSnapshotDTO edition = bookClient.findEdition(copy.getEditionId());
        BookSnapshotDTO book = bookClient.findBook(edition.getBookId());

        Loan loan = Loan.builder().
                editionCopyId(loanRequestDTO.getEditionCopyId()).
                bookTitle(book.getTitle()).
                author(book.getAuthor()).
                edition(edition.getEdition()).
                barcode(copy.getBarcode()).
                memberId(member.getId()).
                memberFirstName(member.getFirstName()).
                memberLastName(member.getLastName()).
                build();
        loan.calculateDueDate(loanPeriodDays);
        Loan saved = repository.save(loan);

        eventPublisher.publishEvent(loanCreatedEvent(saved));
        return Mapper.map(saved, LoanResponseDTO.class);
    }

    /**
     * Renews an existing loan.
     * @param id The id of the loan to renew.
     * @return A {@link LoanResponseDTO} containing details of the renewed loan.
     * @throws ConflictException
     * @throws ResourceNotFoundException Thrown if the loan with the specified id could not be found.
     * @throws IllegalArgumentException Thrown if the provided id is null or not a positive non-zero value.
     */
    @Override
    public LoanResponseDTO renewLoan(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        validateId(id);
        Loan loan = findById(id);
        if(!loan.getStatus().isActive()){
            throw new ConflictException(String.format("Only loans with status %s or %s can be renewed",
                    LoanStatus.BORROWED, LoanStatus.RENEWED));
        }
        if(loan.getRenewalCount() >= renewalLimit){
            throw new ConflictException("The maximum number of renewals has been reached for this loan");
        }
        loan.renewLoan(loanPeriodDays);
        Loan renewed = repository.save(loan);
        return Mapper.map(renewed, LoanResponseDTO.class);
    }

    /**
     * Returns a loaned book edition copy.
     *
     * @param id The id of the loan to return.
     * @return A {@link LoanResponseDTO} containing details of the returned loan.
     * @throws ConflictException if the loan is not in a state that allows it to be returned.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    @Transactional
    public LoanResponseDTO returnLoan(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        Loan loan = findById(id);
        loan.returnLoan();
        Loan saved = repository.save(loan);
        eventPublisher.publishEvent(loanReturnedEvent(saved));
        return Mapper.map(saved, LoanResponseDTO.class);
    }

    /**
     * Marks a loaned book edition copy as lost.
     *
     * @param id The id of the loan to mark as lost.
     * @return A {@link LoanResponseDTO} containing details of the lost loan.
     * @throws ConflictException if the loan is not in a state that allows it to be marked as lost.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    @Transactional
    public LoanResponseDTO markLost(Long id) throws ConflictException, ResourceNotFoundException, IllegalArgumentException {
        Loan loan = findById(id);
        loan.markLost();
        Loan saved = repository.save(loan);
        eventPublisher.publishEvent(loanLostEvent(saved));
        return Mapper.map(saved, LoanResponseDTO.class);
    }

    /**
     * Finds a loan by its unique id.
     *
     * @param id The id of the loan to find.
     * @return A {@link LoanResponseDTO} containing details of the found loan.
     * @throws ResourceNotFoundException if the loan with the specified id could not be found.
     * @throws IllegalArgumentException if the provided id is null or not a positive non-zero value.
     */
    @Override
    public LoanResponseDTO find(Long id) throws ResourceNotFoundException, IllegalArgumentException {
        return Mapper.map(findById(id), LoanResponseDTO.class);
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
        return LoanEventContext.builder().loanId(loan.getId()).
                editionCopyId(loan.getEditionCopyId()).
                memberId(loan.getMemberId()).build();
    }

    private void validateId(Long id){
        if(id == null || id <= 0){
            throw new IllegalArgumentException("The loan id must be a positive non-zero value");
        }
    }

    private Loan findById(Long id){
        validateId(id);
        return repository.findById(id).
                orElseThrow(
                        ()-> new ResourceNotFoundException(String.format("The loan with the id %s could not be found", id)
                        )
                );
    }
}
