package au.com.library.book.service;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.dto.EditionCopyStatusDTO;
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
     * Updates the {@link au.com.library.book.entity.EditionCopyStatus status} of an {@link au.com.library.book.entity.EditionCopy edition copy}.
     * Will be called when:
     * <ul>
     *     <li>An {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available} copy is borrowed
     *     and marked as {@link au.com.library.book.entity.EditionCopyStatus#LOANED loaned}.
     *     </li>
     *     <li>A borrowed copy is returned and set back to {@link au.com.library.book.entity.EditionCopyStatus#AVAILABLE available}.</li>
     *     <li>An unreturned or otherwise missing copy is marked as {@link au.com.library.book.entity.EditionCopyStatus#LOST lost}.</li>
     * </ul>
     * <p>If the status is unchanged, no update will occur.</p>
     * <p>If an edition copy is marked as {@link au.com.library.book.entity.EditionCopyStatus#LOST} its status cannot be changed.
     * If an attempt is made, a {@link au.com.library.shared.exception.BadRequestException} will be thrown.</p>
     *
     * @param editionId The id of the {@link au.com.library.book.entity.Edition edition} to which the copy is linked.
     * @param copyId The id of the {@link au.com.library.book.entity.EditionCopy copy}.
     * @param statusDTO An {@link EditionCopyStatusDTO} object containing the status to set.
     * @return An {@link EditionCopyStatusDTO} object containing the copy details and its new status.
     */
    EditionCopyDTO updateCopyStatus(Long editionId, Long copyId, EditionCopyStatusDTO statusDTO);
    /**
     * Handles the retrieval of a single set of edition copy details.
     * @param editionId The id of the edition linked to the copy.
     * @param copyId The id of the edition copy.
     * @return An {@link EditionCopyDTO} object containing the edition copy details.
     * @throws ResourceNotFoundException Thrown when either the edition or edition copy could not be found.
     */
    EditionCopyDTO findCopy(Long editionId, Long copyId) throws ResourceNotFoundException;

    /**
     * Handles the retrieval of all {@link au.com.library.book.entity.EditionCopy copies} of an {@link au.com.library.book.entity.Edition edition}.
     * @param editionId The edition id.
     * @return A {@link List} of {@link EditionCopyDTO} objects, each representing a copy of an edition.
     * @throws ResourceNotFoundException
     */
    List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException;

}
