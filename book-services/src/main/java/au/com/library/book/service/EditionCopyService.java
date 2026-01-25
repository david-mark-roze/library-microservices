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
     * Marks an edition copy as being {@link au.com.library.book.entity.EditionCopyStatus#LOANED on loan}.
     * @param copyId The id of the edition copy being borrowed.
     * @throws ResourceNotFoundException Thrown when the edition copy to be borrowed could not be found.
     */
    void borrowCopy(Long copyId) throws ResourceNotFoundException;

    /**
     * Marks an edition copy as being {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available}
     * after it has been returned.
     * @param copyId The id of the edition copy being returned.
     * @throws ResourceNotFoundException Thrown when details of the edition copy being returned could not be found in the system.
     */
    void returnCopy(Long copyId) throws ResourceNotFoundException;

    /**
     * Marks an edition copy as being {@link au.com.library.book.entity.EditionCopyStatus#LOST lost}
     * and therefore no longer available.
     * @param copyId The id of the lost edition copy.
     * @throws ResourceNotFoundException Thrown when details of the lost edition copy could not be found in the system.
     */
    void markCopyLost(Long copyId) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of a single set of edition copy details.
     * @param copyId The id of the edition copy.
     * @return An {@link EditionCopyDTO} object containing the edition copy details.
     * @throws ResourceNotFoundException Thrown when either the edition or edition copy could not be found.
     */
    EditionCopyDTO findCopy(Long copyId) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of all {@link au.com.library.book.entity.EditionCopy copies} of an {@link au.com.library.book.entity.Edition edition}.
     * @param editionId The edition id.
     * @return A {@link List} of {@link EditionCopyDTO} objects, each representing a copy of an edition.
     * @throws ResourceNotFoundException Thrown when the edition with the specified id could not be found.
     */
    List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException;

}
