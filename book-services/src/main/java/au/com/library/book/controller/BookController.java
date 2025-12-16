package au.com.library.book.controller;

import au.com.library.book.dto.BookDTO;
import au.com.library.book.dto.EditionDTO;
import au.com.library.book.service.BookService;
import au.com.library.book.service.EditionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * A REST API controller class handling REST APIs to library book operations, as well as the
 * creation and retrieval of edition details.
 *
 * @see BookService
 * @see EditionService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private EditionService editionService;

    /**
     * Handles a REST API POST request to create a library book.
     * @param bookDTO A BookDTO object containing the data for the new book.
     * @return A {@link ResponseEntity} object containing the new book data.
     */
    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO){
        return new ResponseEntity<BookDTO>(service.addBook(bookDTO), HttpStatus.CREATED);
    }

    /**
     * Handles a REST API PUT request to replace the details of a library book with the provided details.
     * @param id The id of the book whose details are to be replaced.
     * @param bookDTO A BookDTO object containing the replacement data for the book.
     * @return A {@link ResponseEntity} object containing the book data after the replacement.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){
        return ResponseEntity.ok(service.updateBook(id, bookDTO));
    }


    /**
     * Handles a REST API GET request to retrieve the details of the library book with the specified id.
     * @param id The id of the book to retrieve.
     * @return A {@link ResponseEntity} object containing the retrieved book details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> findBook(@PathVariable Long id){
        return ResponseEntity.ok(service.findBook(id));
    }

    /**
     * Handles a REST API POST request to create a new edition record for a book.
     * @param bookId The id of the book to that will be linked to the new edition.
     * @param editionDTO An {@link EditionDTO} object containing the new edition details.
     * @return A {@link ResponseEntity} object containing the new edition details.
     */
    @PostMapping("/{bookId}/editions")
    public ResponseEntity<EditionDTO> addEdition(@PathVariable Long bookId, @RequestBody EditionDTO editionDTO){
        return new ResponseEntity<EditionDTO>(editionService.addEdition(bookId, editionDTO), HttpStatus.CREATED);
    }


    /**
     * Handles a REST API GET request to retrieve all editions for a book.
     * @param bookId The id of the book to that will be linked to the new edition.
     * @return A {@link ResponseEntity} object containing the new edition details.
     */
    @GetMapping("/{bookId}/editions")
    public ResponseEntity<Collection<EditionDTO>> findEditions(@PathVariable Long bookId){
        return ResponseEntity.ok(editionService.findEditions(bookId));
    }
}
