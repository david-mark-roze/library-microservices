package au.com.library.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A data transfer object containing {@link au.com.library.book.entity.Book book} data for
 * REST API requests and responses.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO {

    private Long id;
    private String title;
    private String author;
}
