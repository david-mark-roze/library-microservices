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

    @PostMapping("/{id}/borrow")
    public ResponseEntity<EditionCopyDTO> borrowCopy(@PathVariable Long id){
        return ResponseEntity.ok(editionCopyService.borrowCopy(null, id));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<EditionCopyDTO> returnCopy(@PathVariable Long id){
        return ResponseEntity.ok(editionCopyService.returnCopy(null, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditionCopyDTO> findCopy(@PathVariable Long id){
        return ResponseEntity.ok(editionCopyService.findCopy(null, id));
    }
}
