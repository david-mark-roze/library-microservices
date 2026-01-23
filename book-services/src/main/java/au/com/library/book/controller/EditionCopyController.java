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
     * Handles a REST API POST for borrowing an edition copy.
     *
     * @param id The id of the edition copy to borrow.
     * @return A {@link ResponseEntity} containing a {@link EditionCopyDTO}
     * object containing details of the borrowed edition copy.
     */
//    @PostMapping("/{id}/borrow")
//    public ResponseEntity<EditionCopyDTO> borrowCopy(@PathVariable Long id){
//        return ResponseEntity.ok(editionCopyService.borrowCopy(id));
//    }

    /**
     * Handles a REST API POST for returning an edition copy.
     *
     * @param id The id of the edition copy to return.
     * @return A {@link ResponseEntity} containing a {@link EditionCopyDTO}
     * object containing details of the returned edition copy.
     */
//    @PostMapping("/{id}/return")
//    public ResponseEntity<EditionCopyDTO> returnCopy(@PathVariable Long id){
//        return ResponseEntity.ok(editionCopyService.returnCopy(id));
//    }

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

    /**
     * Handles a REST API POST for marking an edition copy as lost.
     *
     * @param id The id of the edition copy to mark as lost.
     * @return A {@link ResponseEntity} containing a {@link EditionCopyDTO}
     * object containing details of the lost edition copy.
     */
//    @PostMapping("/{id}/lost")
//    public ResponseEntity<EditionCopyDTO> markCopyLost(@PathVariable Long id){
//        return ResponseEntity.ok(editionCopyService.markCopyLost(id));
//    }
}
