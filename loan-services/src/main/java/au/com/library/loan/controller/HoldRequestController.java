package au.com.library.loan.controller;

import au.com.library.loan.dto.HoldRequestDTO;
import au.com.library.loan.dto.HoldRequestInputDTO;
import au.com.library.loan.service.HoldRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles REST API requests for library loan hold requests.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/holds")
public class HoldRequestController {

    private final HoldRequestService service;

    /**
     * Handles a REST API POST for placing a loan hold request for a member on a specific edition.
     *
     * @param holdRequestInput A {@link HoldRequestInputDTO} object containing the data required for placing a hold request.
     * @return A {@link ResponseEntity} containing a  {@link HoldRequestDTO} object containing details of the new hold request.
     */
    @PostMapping
    public ResponseEntity<HoldRequestDTO> placeHoldRequest(@RequestBody HoldRequestInputDTO holdRequestInput) {
        HoldRequestDTO result = service.placeHoldRequest(holdRequestInput.memberId(), holdRequestInput.editionId());
        return new ResponseEntity<HoldRequestDTO>(result, HttpStatus.CREATED);
    }

    /**
     * Handles a REST API POST for cancelling a hold request.
     *
     * @param id The id of the hold request to cancel.
     * @return A {@link ResponseEntity} containing a {@link HoldRequestDTO}
     * object containing details of the canceled hold request.
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<HoldRequestDTO> cancelHoldRequest(@PathVariable Long id) {
        HoldRequestDTO result = service.cancelHoldRequest(id);
        return ResponseEntity.ok(result);
    }
}
