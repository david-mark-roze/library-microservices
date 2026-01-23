package au.com.library.book.controller;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.service.EditionCopyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/copies")
public class EditionCopyController {

    private EditionCopyService editionCopyService;

    /**
     * Handles a REST API GET to find an edition copy by its unique id.
     * @param id The edition copy id.
     * @return A {@link ResponseEntity} containing a {@link EditionCopyDTO}
     * object containing details of the found edition copy.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EditionCopyDTO> findCopy(@PathVariable Long id){
        return ResponseEntity.ok(editionCopyService.findCopy(id));
    }
}
