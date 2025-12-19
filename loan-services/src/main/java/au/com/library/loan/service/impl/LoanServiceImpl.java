package au.com.library.loan.service.impl;

import au.com.library.loan.dto.*;
import au.com.library.loan.entity.Loan;
import au.com.library.loan.entity.LoanStatus;
import au.com.library.loan.exception.CopyUnavailableException;
import au.com.library.loan.repository.LoanRepository;
import au.com.library.loan.service.BookClient;
import au.com.library.loan.service.LoanService;
import au.com.library.loan.service.MemberClient;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * The {@link LoanService} implementation.
 */
@Service
public class LoanServiceImpl implements LoanService {

    private Logger LOGGER = LoggerFactory.getLogger(LoanServiceImpl.class);

    private BookClient bookClient;
    private MemberClient memberClient;

    private LoanRepository repository;

    public LoanServiceImpl(BookClient bookClient, MemberClient memberClient, LoanRepository repository) {
        this.bookClient = bookClient;
        this.memberClient = memberClient;
        this.repository = repository;
    }

    @Value("${loan.period-days}")
    private int loanPeriodDays;

    @Override
    public LoanResponseDTO createLoan(LoanRequestDTO loanRequestDTO) throws CopyUnavailableException {

        EditionCopySnapshotDTO copy = bookClient.findEditionCopy(loanRequestDTO.getEditionId(), loanRequestDTO.getEditionCopyId());
        if(!copy.getStatus().isAvailable()) {
            throw new CopyUnavailableException("The copy of the book requested is unavailable");
        }

        MemberSnapshotDTO member = memberClient.findMember(loanRequestDTO.getMemberId());
        EditionSnapshotDTO edition = bookClient.findEdition(loanRequestDTO.getEditionId());
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
                loanDate(LocalDate.now()).
                status(LoanStatus.BORROWED).
                build();
        loan.calculateDueDate(loanPeriodDays);
        Loan saved = repository.save(loan);
        return Mapper.map(saved, LoanResponseDTO.class);
    }

    @Override
    public LoanResponseDTO returnLoan(Long id) {
        Loan loan = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The loan with the id %s could not be found"));
        loan.returnLoan();
        return Mapper.map(repository.save(loan), LoanResponseDTO.class);
    }
}
