package au.com.library.book.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * This represents a copy of an {@link Edition book edition} at the library
 * of which there may be more than one.
 *
 * @see Edition
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class EditionCopy {

    private static final int LENGTH_STATUS = 50;
    private static final int LENGTH_BARCODE = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            updatable = false,
            unique = true,
            length = LENGTH_BARCODE
    )
    private String barcode;

    @Setter
    @Column(nullable = false, length = LENGTH_STATUS)
    @Enumerated(EnumType.STRING)
    private EditionCopyStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime dateAcquired;

    @Setter
    // Bidirectional many to one - This child EditionCopy references its parent Edition via
    // the 'edition_id' foreign key. In the parent, this is the 'id' column.
    @JoinColumn(name = "edition_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Edition edition;

}
