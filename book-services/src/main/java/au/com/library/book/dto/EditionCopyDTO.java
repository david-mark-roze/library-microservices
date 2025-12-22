package au.com.library.book.dto;

import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    public static EditionCopyDTO toDTO(EditionCopy editionCopy){
        EditionCopyDTO dto = Mapper.map(editionCopy, EditionCopyDTO.class);
        dto.setEditionId(editionCopy.getEdition().getId());
        return dto;
    }
}
