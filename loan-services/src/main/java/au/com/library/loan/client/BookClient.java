package au.com.library.loan.client;

import au.com.library.loan.client.config.ClientErrorDecoderConfig;
import au.com.library.loan.dto.BookSnapshotDTO;
import au.com.library.loan.dto.EditionCopySnapshotDTO;
import au.com.library.loan.dto.EditionSnapshotDTO;
import au.com.library.loan.dto.LoanResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * A Feign Client interface handling calls to the books/book editions service.
 */
@FeignClient(
        name="book-services",
        configuration = ClientErrorDecoderConfig.class
)
public interface BookClient {

    /**
     * Finds a book by its unique id.
     *
     * @param bookId The book id.
     * @return A {@link BookSnapshotDTO} object containing the book details.
     */
    @GetMapping("/api/books/{bookId}")
    BookSnapshotDTO findBook(@PathVariable Long bookId);

    /**
     * Finds a book edition by its unique id.
     *
     * @param editionId The edition id.
     * @return A {@link EditionSnapshotDTO} object containing the edition details.
     */
    @GetMapping("/api/editions/{editionId}")
    EditionSnapshotDTO findEdition(@PathVariable Long editionId);

    /**
     * Finds a book edition copy by its unique id.
     *
     * @param id The edition copy id.
     * @return A {@link EditionCopySnapshotDTO} object containing the edition copy details.
     */
    @GetMapping("/api/copies/{id}")
    EditionCopySnapshotDTO findCopy(@PathVariable Long id);

    /**
     * Finds a book edition copy with the specified id and marks it
     * as {@link au.com.library.loan.dto.EditionCopyStatus#LOANED on loan}.
     *
     * @param id The edition copy id.
     * @return A {@link EditionCopySnapshotDTO} object containing the updated edition copy details.
     */
    @PostMapping("/api/copies/{id}/borrow")
    EditionCopySnapshotDTO borrowCopy(@PathVariable Long id);

    /**
     * Returns a book edition copy with the specified id.
     *
     * @param id The edition copy id.
     * @return A {@link EditionCopySnapshotDTO} object containing the updated edition copy details.
     */
    @PostMapping("/api/copies/{id}/return")
    EditionCopySnapshotDTO returnCopy(@PathVariable Long id);

    /**
     * Marks a book edition copy with the specified id as lost.
     *
     * @param id The edition copy id.
     * @return A {@link EditionCopySnapshotDTO} object containing the updated edition copy details.
     */
    @PostMapping("/api/copies/{id}/lost")
    EditionCopySnapshotDTO markCopyLost(@PathVariable Long id);

}
