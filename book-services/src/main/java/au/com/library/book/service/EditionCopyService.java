package au.com.library.book.service;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.shared.exception.ResourceNotFoundException;

import java.util.List;

/**
 * A service level interface handling the creation, update and retrieval of {@link au.com.library.book.entity.EditionCopy
 * edition copy} details.
 */
public interface EditionCopyService {

    /**
     * Handles the creation of a {@link au.com.library.book.entity.EditionCopy copy} of
     * an {@link au.com.library.book.entity.Edition edition} of a
     * {@link au.com.library.book.entity.Book book}.
     *
     * @param editionId The id of the edition to which the copy will be linked.
     * @return A {@link EditionCopyDTO} object containing the new copy details.
     * @throws ResourceNotFoundException Thrown when the edition could not be found.
     */
    EditionCopyDTO addCopy(Long editionId) throws ResourceNotFoundException;

    /**
     * Handles the update/replacement of {@link au.com.library.book.entity.EditionCopy editon copy} details.
     * @param copyId The id of the edition copy.
     * @param copyDTO An {@link EditionCopyDTO} containing the edition copy update details.
     * @return An {@link EditionCopyDTO} object containing the updated copy details.
     * @throws ResourceNotFoundException Thrown when the edition copy to update could not be found.
     */
    EditionCopyDTO updateCopy(Long copyId, EditionCopyDTO copyDTO) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of a single set of edition copy details.
     * @param copyId The id of the edition copy.
     * @return An {@link EditionCopyDTO} object containing the edition copy details.
     * @throws ResourceNotFoundException Thrown when the edition copy could not be found.
     */
    EditionCopyDTO findCopy(Long copyId) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of all {@link au.com.library.book.entity.EditionCopy copies} of an {@link au.com.library.book.entity.Edition edition}.
     * @param editionId The edition id.
     * @return A {@link List} of {@link EditionCopyDTO} objects, each representing a copy of an edition.
     * @throws ResourceNotFoundException
     */
    List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException;

}
