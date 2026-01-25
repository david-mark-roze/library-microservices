package au.com.library.book.dto;

import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for {@link EditionCopy edition copies}.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditionCopyDTO {

    private Long id;
    private String barcode;
    private EditionCopyStatus status;
    private LocalDateTime dateAcquired;
    private Long editionId;

    /**
     * Maps an {@link EditionCopy edition copy} entity to an {@link EditionCopyDTO edition copy DTO}.
     *
     * @param editionCopy The edition copy entity to be mapped.
     * @return An {@link EditionCopyDTO} object containing the mapped data.
     */
    public static EditionCopyDTO toDTO(EditionCopy editionCopy){
        EditionCopyDTO dto = Mapper.map(editionCopy, EditionCopyDTO.class);
        dto.setEditionId(editionCopy.getEdition().getId());
        return dto;
    }
}
