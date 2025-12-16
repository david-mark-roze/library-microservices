package au.com.library.book.service;

import au.com.library.book.dto.EditionDTO;
import au.com.library.shared.exception.ResourceNotFoundException;

/**
 * The service level interface handling the retrieval and update of {@link au.com.library.book.entity.Edition edition} details.
 */
public interface EditionService {

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
