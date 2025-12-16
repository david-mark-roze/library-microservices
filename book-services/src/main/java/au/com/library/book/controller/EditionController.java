package au.com.library.book.controller;

import au.com.library.book.dto.EditionDTO;
import au.com.library.book.service.EditionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A REST API controller class handling REST APIs to library book edition operations.
 *
 * @see EditionService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/editions")
public class EditionController {

    private EditionService service;

    @GetMapping("/{id}")
    public ResponseEntity<EditionDTO> findEdition(@PathVariable Long id){
        return ResponseEntity.ok(service.findEdition(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditionDTO> updateEdition(@PathVariable Long id, @RequestBody EditionDTO editionDTO) {
        return ResponseEntity.ok(service.updateEdtion(id, editionDTO));
    }
}
