package au.com.library.book.dto;

import au.com.library.book.entity.EditionCopyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * A data transfer object used to update the {@link EditionCopyStatus status} of
 * an {@link au.com.library.book.entity.EditionCopy edition copy}.
 *
 */
@AllArgsConstructor
@Getter
public class EditionCopyStatusDTO {

    private EditionCopyStatus status;
}
