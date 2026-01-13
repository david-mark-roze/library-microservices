package au.com.library.loan.controller;

import au.com.library.loan.dto.LoanRequestDTO;
import au.com.library.loan.dto.LoanResponseDTO;
import au.com.library.loan.service.LoanService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles REST API requests for library book loans.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private LoanService service;

    /**
     * Handles a REST API POST for creating a library book loan.
     *
     * @param loanRequestDTO A {@link LoanRequestDTO} object containing the data required for creating a loan.
     * @return A {@link ResponseEntity} containing a  {@link LoanResponseDTO} object containing details of the new loan.
     */
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO loanRequestDTO){
        return new ResponseEntity<LoanResponseDTO>(service.createLoan(loanRequestDTO), HttpStatus.CREATED);
    }
    /**
     * Handles a REST API POST for returning a loan.
     *
     * @param id A The id of the loan to return.
     * @return A {@link ResponseEntity} containing a {@link LoanResponseDTO}
     * object containing details of the loan returned.
     */
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnLoan(@PathVariable Long id){
        return ResponseEntity.ok(service.returnLoan(id));
    }

    /**
     * Handles a REST API POST for renewing a loan.
     *
     * @see au.com.library.loan.service.LoanService#renewLoan(Long)
     *
     * @param id A The id of the loan to renew.
     * @return A {@link ResponseEntity} containing a {@link LoanResponseDTO}
     * object containing details of the renewed loan.
     */
    @PostMapping("/{id}/renew")
    public ResponseEntity<LoanResponseDTO> renewLoan(@PathVariable Long id){
        return ResponseEntity.ok(service.renewLoan(id));
    }

    /**
     * Handles a REST API GET to find {@link au.com.library.loan.entity.Loan loan} by its
     * unique id.
     * @param id The loan id.
     * @return A {@link ResponseEntity} object containing a {@link LoanResponseDTO} object containing
     * the details of the returned loan.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> findLoan(@PathVariable Long id){
        return ResponseEntity.ok(service.find(id));
    }
}
