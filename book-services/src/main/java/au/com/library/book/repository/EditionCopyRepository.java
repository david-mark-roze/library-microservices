package au.com.library.book.repository;

import au.com.library.book.entity.EditionCopy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@link JpaRepository} extension for {@link EditionCopy} entities.
 */
public interface EditionCopyRepository extends JpaRepository<EditionCopy, Long> {
}
