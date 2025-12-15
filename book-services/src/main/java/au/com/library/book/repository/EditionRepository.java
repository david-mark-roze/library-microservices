package au.com.library.book.repository;

import au.com.library.book.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The {@link JpaRepository} extension for {@link Edition} entities.
 */
public interface EditionRepository extends JpaRepository<Edition, Long> {

    /**
     * Retrieves all editions for a book.
     * @param bookId The id of the book.
     * @return A {@link List} of {@link Edition editions} or an empty List if none are recorded yet.
     */
    List<Edition> findByBookId(Long bookId);
}
