package au.com.library.book.controller;

import au.com.library.book.BookDTO;
import au.com.library.book.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST API controller class handling REST APIs to library book operations.
 *
 * @see BookService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;

    /**
     * Handles a REST API POST request to create a library book.
     * @param bookDTO A BookDTO object containing the data for the new book.
     * @return A {@link ResponseEntity} object containing the new book data.
     */
    @PostMapping
    public ResponseEntity<BookDTO> add(@RequestBody BookDTO bookDTO){
        return new ResponseEntity<BookDTO>(service.add(bookDTO), HttpStatus.CREATED);
    }
}
