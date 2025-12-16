package au.com.library.book.service;

import au.com.library.book.dto.EditionDTO;
import au.com.library.shared.exception.ResourceNotFoundException;

import java.util.Collection;

/**
 * The service level interface handling addition, update and retrieval of {@link au.com.library.book.entity.Edition edition} details.
 */
public interface EditionService {
    /**
     * Handles the creation of the {@link au.com.library.book.entity.Edition edition} of a {@link au.com.library.book.entity.Book book}.
     * @param bookId The id of the associated book details.
     * @param editionDTO The edition details.
     * @return An {@link EditionDTO} object containing the details of the creation edition.
     * @throws ResourceNotFoundException Thrown when the associated book could not be found.
     */
    EditionDTO addEdition(Long bookId, EditionDTO editionDTO) throws ResourceNotFoundException;

    Collection<EditionDTO> findEditions(Long bookId) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of book edition details.
     * @param id The edition id.
     * @return An {@link EditionDTO} object containing the {@link au.com.library.book.entity.Edition edition} details.
     * @throws ResourceNotFoundException Thrown when the edition could not be found.
     */
    EditionDTO findEdition(Long id) throws ResourceNotFoundException;

    /**
     * Handles the update/replacement of a set of {@link au.com.library.book.entity.Edition edition} details.
     * @param id The id of the edition to update.
     * @param editionDTO An {@link EditionDTO} object containing the update/replacement details.
     * @return An {@link EditionDTO} object containing the updated details.
     * @throws ResourceNotFoundException Thrown when the edition to update could not be found.
     */
    EditionDTO updateEdtion(Long id, EditionDTO editionDTO) throws ResourceNotFoundException;
}
