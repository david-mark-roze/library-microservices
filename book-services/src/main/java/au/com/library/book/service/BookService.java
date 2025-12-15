package au.com.library.book.service;

import au.com.library.book.dto.BookDTO;
import au.com.library.book.dto.EditionDTO;
import au.com.library.shared.exception.ResourceNotFoundException;

import java.util.Collection;

/**
 * The service level interface for handling {@link au.com.library.book.entity.Book books}.
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
    BookDTO update(Long id, BookDTO bookDTO) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of library book details.
     * @param id The book id.
     * @return A {@link BookDTO} object containing the book details.
     * @throws ResourceNotFoundException Thrown when a book with the specified id could not be found.
     */
    BookDTO find(Long id) throws ResourceNotFoundException;

    /**
     * Handles the creation of the {@link au.com.library.book.entity.Edition edition} of a {@link au.com.library.book.entity.Book book}.
     * @param bookId The id of the associated book details.
     * @param editionDTO The edition details.
     * @return An {@link EditionDTO} object containing the details of the creation edition.
     * @throws ResourceNotFoundException Thrown when the associated book could not be found.
     */
    EditionDTO addEdition(Long bookId, EditionDTO editionDTO) throws ResourceNotFoundException;

    Collection<EditionDTO> findEditions(Long bookId) throws ResourceNotFoundException;
}
