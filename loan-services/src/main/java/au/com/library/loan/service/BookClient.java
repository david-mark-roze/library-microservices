package au.com.library.loan.service;

import au.com.library.loan.dto.BookSnapshotDTO;
import au.com.library.loan.dto.EditionCopySnapshotDTO;
import au.com.library.loan.dto.EditionSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="book-services", url = "${services.books.base-url}")
public interface BookClient {

    @GetMapping("/api/books/{bookId}")
    BookSnapshotDTO findBook(@PathVariable Long bookId);

    @GetMapping("/api/editions/{editionId}")
    EditionSnapshotDTO findEdition(@PathVariable Long editionId);

    @GetMapping("/api/copies/{id}")
    EditionCopySnapshotDTO findCopy(@PathVariable Long id);

    @PostMapping("/api/copies/{id}/borrow")
    EditionCopySnapshotDTO borrowCopy(@PathVariable Long id);

    @PostMapping("/api/copies/{id}/return")
    EditionCopySnapshotDTO returnCopy(@PathVariable Long id);

}
