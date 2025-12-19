package au.com.library.book.dto;

import au.com.library.book.entity.BookFormat;
import au.com.library.book.entity.Edition;
import au.com.library.shared.util.Mapper;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * A data transfer object containing {@link au.com.library.book.entity.Edition edtion} data for
 * REST API requests and responses, including the persistence of editions, the retrieval of editions
 * and the retrieval of books which contain references to editions.
 *
 * @author David Roze
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditionDTO {

    private Long id;
    private String isbn;
    private String publisher;
    private Integer publicationYear;
    private String edition;
    private BookFormat format;
    private Set<EditionCopyDTO> copies = new HashSet<>();
    private Long bookId;

    /**
     * Maps an {@link Edition} object to an {@link EditionDTO} object,
     * also setting the {@link #getBookId() bookId} attribute from its {@link Edition#getBook() book reference}.
     * @param edition The source {@link Edition} object.
     * @return The created {@link EditionDTO} object.
     */
    public static EditionDTO toDTO(Edition edition){
        EditionDTO editionDTO = Mapper.map(edition, EditionDTO.class);
        editionDTO.setBookId(edition.getBook().getId());
        return editionDTO;
    }
}
