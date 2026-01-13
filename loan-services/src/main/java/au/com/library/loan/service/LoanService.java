package au.com.library.loan.service;

import au.com.library.loan.dto.LoanRequestDTO;
import au.com.library.loan.dto.LoanResponseDTO;
import au.com.library.loan.exception.CopyUnavailableException;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ResourceNotFoundException;

public interface LoanService {

    /**
     * Handles the creation of a {@link au.com.library.loan.entity.Loan library book loan}. The {@link LoanRequestDTO loanRequestDTO} parameter
     * contains
     * <ul>
     *      <li>The id of the book edition</li>
     *      <li>The id of the edition copy</li>
     *      <li>The {@link au.com.library.loan.dto.EditionCopyStatus status} of the edition copy.</li>
     *  </ul>
     *  These will be used, along additional data from the book and member services, to create a {@link au.com.library.loan.entity.Loan loan}.
     *
     * @param loanRequestDTO A {@link LoanRequestDTO} object containing the above data.
     * @return A {@link LoanResponseDTO} object containing the new loan details.
     * @throws CopyUnavailableException Thrown when the request copy of a book is unavailable.
     */
    LoanResponseDTO createLoan(LoanRequestDTO loanRequestDTO) throws CopyUnavailableException;

    /**
     * Handles the renewal of an existing loan. A renewal may only be performed if the loan is current (i.e {@link au.com.library.loan.entity.LoanStatus#BORROWED borrowed} or
     * {@link#LoanStatus#RENEWED renewed}) and has not exceeded the maximum number of renewals.
     * @param id The id of the loan to renew.
     * @return A {@link LoanResponseDTO} object containing details of the renewed loan.
     * @throws ConflictException Thrown when the loan is not in a state that allows renewal or has exceeded the maximum number of renewals.
     */
    LoanResponseDTO renewLoan(Long id) throws ConflictException;

    /**
     * Handles the returning of a loan. The {@link au.com.library.loan.entity.LoanStatus} will be changed to {@link au.com.library.loan.entity.LoanStatus#RETURNED returned} and populated with a return date.
     * @param id The id of the returned loan.
     * @return A {@link LoanResponseDTO} object containing details of the returned loan.
     * @throws ConflictException Thrown when the loan has already been returned.
     */
    LoanResponseDTO returnLoan(Long id) throws ConflictException;

    /**
     * Handles a query to find a {@link au.com.library.loan.entity.Loan loan} by its id.
     * @param id The loan id.
     * @return A {@link LoanResponseDTO} object containing the loan details.
     * @throws au.com.library.shared.exception.ResourceNotFoundException Thrown when the loan details could not be found.
     */
    LoanResponseDTO find(Long id) throws ResourceNotFoundException;
}
