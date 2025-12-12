package au.com.library.book.repository;

import au.com.library.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@link JpaRepository} extension for {@link Book} entities.
 */
public interface BookRespository extends JpaRepository<Book, Long> {
}
