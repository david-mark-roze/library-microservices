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
     * @return A {@link LoanResponseDTO} object containing details of the new loan.
     */
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO loanRequestDTO){
        return new ResponseEntity<LoanResponseDTO>(service.createLoan(loanRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponseDTO> returnLoan(@PathVariable Long id){
        return ResponseEntity.ok(service.returnLoan(id));
    }
}
