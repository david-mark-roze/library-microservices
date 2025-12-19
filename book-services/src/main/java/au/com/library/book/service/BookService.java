package au.com.library.book.service;

import au.com.library.book.dto.BookDTO;
import au.com.library.shared.exception.ResourceNotFoundException;

/**
 * The service level interface for handling the addition, update and
 * retrieval of {@link au.com.library.book.entity.Book books}.
 */
public interface BookService {

    /**
     * Handles the creation of a {@link au.com.library.book.entity.Book book}.
     * @param bookDTO Contains the new book data.
     * @return A {@link BookDTO} object containing the new book details.
     */
    BookDTO addBook(BookDTO bookDTO);

    /**
     * Handles the replacement of details for a book with the specified id.
     * @param id The book id.
     * @param bookDTO Contains the replacement details.
     * @return A {@link BookDTO} object containing the updated book details.
     * @throws ResourceNotFoundException Thrown when a book with the specified id could not be found.
     */
    BookDTO updateBook(Long id, BookDTO bookDTO) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of library book details.
     * @param id The book id.
     * @return A {@link BookDTO} object containing the book details.
     * @throws ResourceNotFoundException Thrown when a book with the specified id could not be found.
     */
    BookDTO findBook(Long id) throws ResourceNotFoundException;
}
