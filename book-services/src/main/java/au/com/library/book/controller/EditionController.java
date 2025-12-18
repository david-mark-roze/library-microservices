package au.com.library.book.controller;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.dto.EditionDTO;
import au.com.library.book.service.EditionCopyService;
import au.com.library.book.service.EditionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A REST API controller class handling REST APIs to library book edition operations.
 *
 * @see EditionService
 * @see EditionCopyService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/editions")
public class EditionController {

    private EditionService editionService;
    private EditionCopyService editionCopyService;

    /**
     * Handles a REST API GET to retrieve book edition details by its id.
     * @param id The edition id.
     * @return A {@link ResponseEntity} object that references and {@link EditionDTO} object
     * containing the edition details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EditionDTO> findEdition(@PathVariable Long id){
        return ResponseEntity.ok(editionService.findEdition(id));
    }

    /**
     * Handles a REST API PUT to update book edition details.
     * @param id The id of the edition to update.
     * @param editionDTO An {@link EditionDTO} object containing the edition data submitted for update.
     * @return A {@link ResponseEntity} object that references an {@link EditionDTO} object
     * containing the updated details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EditionDTO> updateEdition(@PathVariable Long id, @RequestBody EditionDTO editionDTO) {
        return ResponseEntity.ok(editionService.updateEdtion(id, editionDTO));
    }

    /**
     * Handles a REST API POST to create an {@link au.com.library.book.entity.EditionCopy edition copy}.
     * While no client data will be submitted, but the copy will be created at the backend with a {@link au.com.library.book.entity.EditionCopyStatus status} of {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE AVAILABLE}.
     *
     * @param editionId The id of the edition for which the copy will be created.
     * @return A {@link ResponseEntity} object that references an {@link EditionCopyDTO} object
     * containing the new copy details.
     */
    @PostMapping("/{editionId}/copies")
    public ResponseEntity<EditionCopyDTO> addCopy(@PathVariable Long editionId){
        return new ResponseEntity<EditionCopyDTO>( editionCopyService.addCopy(editionId), HttpStatus.CREATED);
    }

    /**
     * Handles a REST API GET to retrieve existing edition copy details.
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the copy to retrieve.
     * @return A {@link ResponseEntity} object that references an {@link EditionCopyDTO} object
     * containing the retrieved copy details.
     */
    @GetMapping("/{editionId}/copies/{copyId}")
    public ResponseEntity<EditionCopyDTO> findCopy(@PathVariable Long editionId, @PathVariable Long copyId){
        return ResponseEntity.ok(editionCopyService.findCopy(editionId, copyId));
    }

    /**
     * Handles a REST API GET to retrieve all existing edition copy details.
     * @param editionId The id of the edition linked to each copy.
     * @return A {@link ResponseEntity} object that references a {@link List} of {@link EditionCopyDTO} objects,
     * each representing a retrieved copy.
     */
    @GetMapping("/{editionId}/copies")
    public ResponseEntity<List<EditionCopyDTO>> findCopies(@PathVariable Long editionId){
        return ResponseEntity.ok(editionCopyService.findCopies(editionId));
    }

    /**
     * Handles a REST API POST to mark a copy as 'borrowed' or {@link au.com.library.book.entity.EditionCopyStatus#LOANED loaned}.
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the copy being borrowed.
     * @return A {@link ResponseEntity} object that references an {@link EditionCopyDTO} objects containing details of the borrowed copy.
     */
    @PostMapping("/{editionId}/copies/{copyId}/borrow")
    public ResponseEntity<EditionCopyDTO> borrowCopy(@PathVariable Long editionId, @PathVariable Long copyId){
        return ResponseEntity.ok(editionCopyService.borrowCopy(editionId, copyId));
    }

    /**
     * Handles a REST API POST to mark a copy as 'returned' or {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available}.
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the copy being returned.
     * @return A {@link ResponseEntity} object that references an {@link EditionCopyDTO} objects containing details of the returned copy.
     */
    @PostMapping("/{editionId}/copies/{copyId}/return")
    public ResponseEntity<EditionCopyDTO> returnCopy(@PathVariable Long editionId, @PathVariable Long copyId){
        return ResponseEntity.ok(editionCopyService.returnCopy(editionId, copyId));
    }

    /**
     * Handles a REST API POST to mark a copy as {@link au.com.library.book.entity.EditionCopyStatus#LOST lost}.
     *
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the lost copy.
     * @return A {@link ResponseEntity} object that references an {@link EditionCopyDTO} objects containing details of the lost copy.
     */
    @PostMapping("/{editionId}/copies/{copyId}/mark-lost")
    public ResponseEntity<EditionCopyDTO> markCopyLost(@PathVariable Long editionId, @PathVariable Long copyId){
        return ResponseEntity.ok(editionCopyService.markCopyLost(editionId, copyId));
    }
}
