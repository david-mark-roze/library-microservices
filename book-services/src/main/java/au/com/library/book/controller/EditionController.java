package au.com.library.book.controller;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.dto.EditionCopyStatusDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<EditionDTO> findEdition(@PathVariable Long id){
        return ResponseEntity.ok(editionService.findEdition(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditionDTO> updateEdition(@PathVariable Long id, @RequestBody EditionDTO editionDTO) {
        return ResponseEntity.ok(editionService.updateEdtion(id, editionDTO));
    }

    @PostMapping("/{editionId}/copies")
    public ResponseEntity<EditionCopyDTO> addCopy(@PathVariable Long editionId){
        return new ResponseEntity<EditionCopyDTO>( editionCopyService.addCopy(editionId), HttpStatus.CREATED);
    }

    @PatchMapping("/{editionId}/copies/{copyId}/status")
    public ResponseEntity<EditionCopyDTO> updateCopyStatus(@PathVariable Long editionId, @PathVariable Long copyId, @RequestBody EditionCopyStatusDTO status){
        return ResponseEntity.ok(editionCopyService.updateCopyStatus(editionId, copyId, status));
    }

    @GetMapping("/{editionId}/copies")
    public ResponseEntity<List<EditionCopyDTO>> findCopies(@PathVariable Long editionId){
        return ResponseEntity.ok(editionCopyService.findCopies(editionId));
    }
}
