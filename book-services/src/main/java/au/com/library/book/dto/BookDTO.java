package au.com.library.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A data transfer object containing {@link au.com.library.book.entity.Book book} data for
 * REST API requests and responses, including the persistence and retrieval of books. The latter
 * may contain references to editions.
 *
 * @author David Roze
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO {

    private Long id;
    private String title;
    private String author;
    private Set<EditionDTO> editions = new HashSet<>();
}
