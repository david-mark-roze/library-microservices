package au.com.library.book.repository;

import au.com.library.book.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The {@link JpaRepository} extension for {@link Edition} entities.
 */
public interface EditionRepository extends JpaRepository<Edition, Long> {
}
