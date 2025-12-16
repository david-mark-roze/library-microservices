package au.com.library.book.service.impl;

import au.com.library.book.dto.BookDTO;
import au.com.library.book.dto.EditionDTO;
import au.com.library.book.entity.Book;
import au.com.library.book.entity.Edition;
import au.com.library.book.repository.BookRespository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.BookService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * The {@link BookService} implementation.
 *
 * @see BookRespository
 */
@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private BookRespository bookRespository;

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        try {
            Book book = bookRespository.save(Mapper.map(bookDTO, Book.class));
            return Mapper.map(book, BookDTO.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) throws ResourceNotFoundException {
        Book book = findBookById(id);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        try {
            Book saved = bookRespository.save(book);
            return Mapper.map(saved, BookDTO.class);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookDTO findBook(Long id) throws ResourceNotFoundException {
        return Mapper.map(findBookById(id), BookDTO.class);
    }

    private Book findBookById(Long id){
        return bookRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("The book with the id %s could not be found", id)));
    }
}
