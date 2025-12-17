package au.com.library.book.repository;

import au.com.library.book.entity.EditionCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The {@link JpaRepository} extension for {@link EditionCopy} entities.
 */
public interface EditionCopyRepository extends JpaRepository<EditionCopy, Long> {

    /**
     * Handles the retrieval of an {@link EditionCopy edition copy}, specifying both the edition id and the copy id.
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the copy.
     * @return An {@link Optional} object containing the retrieved {@link EditionCopy copy}.
     */
    Optional<EditionCopy> findByIdAndEditionId(Long editionId, Long copyId);

    /**
     * Handles the retrieval of all {@link EditionCopy edition copies} for an {@link au.com.library.book.entity.Edition edition}.
     * @param editionId The id of the edition.
     * @return A {@link List} of {@link EditionCopy edition copies} or an empty List if none were found.
     */
    List<EditionCopy> findByEditionId(Long editionId);
}
