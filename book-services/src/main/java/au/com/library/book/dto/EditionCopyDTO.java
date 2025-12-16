package au.com.library.book.dto;

import au.com.library.book.entity.EditionCopyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditionCopyDTO {

    private Long id;
    private String barcode;
    private EditionCopyStatus status;
    private LocalDateTime dateAquired;
}
