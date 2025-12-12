package au.com.library.book.service;

import au.com.library.book.BookDTO;

/**
 * The service level interface for handling {@link au.com.library.book.entity.Book books}.
 */
public interface BookService {

    /**
     * Handles the creation of a {@link au.com.library.book.entity.Book book}.
     * @param bookDTO Contains the new book data.
     * @return A {@link BookDTO} object containing the new book details.
     */
    BookDTO add(BookDTO bookDTO);
}
