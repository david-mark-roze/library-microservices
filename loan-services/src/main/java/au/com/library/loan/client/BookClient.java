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

    @GetMapping("/api/books/{bookId}")
    BookSnapshotDTO findBook(@PathVariable Long bookId);

    @GetMapping("/api/editions/{editionId}")
    EditionSnapshotDTO findEdition(@PathVariable Long editionId);

    @GetMapping("/api/copies/{id}")
    EditionCopySnapshotDTO findCopy(@PathVariable Long id);

    @PostMapping("/api/copies/{id}/borrow")
    LoanResponseDTO borrowCopy(@PathVariable Long id);

    @PostMapping("/api/copies/{id}/return")
    EditionCopySnapshotDTO returnCopy(@PathVariable Long id);

}
