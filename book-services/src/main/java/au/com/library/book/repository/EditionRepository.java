package au.com.library.book.repository;

import au.com.library.book.entity.Edition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditionRepository extends JpaRepository<Edition, Long> {
}
