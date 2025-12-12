package au.com.library.book.service.impl;

import au.com.library.book.BookDTO;
import au.com.library.book.entity.Book;
import au.com.library.book.repository.BookRespository;
import au.com.library.book.service.BookService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * The {@link BookService} implementation.
 *
 * @see BookRespository
 */
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRespository repository;

    @Override
    public BookDTO add(BookDTO bookDTO) {
        try {
            Book book = repository.save(Mapper.map(bookDTO, Book.class));
            return Mapper.map(book, BookDTO.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
