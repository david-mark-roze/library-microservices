package au.com.library.book.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
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

    @Column(nullable = false, length = LENGTH_STATUS)
    @Enumerated(EnumType.STRING)
    private EditionCopyStatus status;

    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime dateAcquired;

    // Bidirectional many to one - This child EditionCopy references its parent Edition via
    // Bidirectional many to one - This child EditionCopy references its parent Edition via
    // the 'edition_id' foreign key. In the parent, this is the 'id' column.
    @JoinColumn(name = "edition_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Edition edition;

    /**
     * Marks this copy as being {@link EditionCopyStatus#LOANED on loan}, if it is not already.
     * @throws IllegalStateException Thrown when this copy is recorded as {@link EditionCopyStatus#LOST lost}.
     */
    public void markBorrowed(){
        checkLost();
        if(status.isAvailable()){
            status = EditionCopyStatus.LOANED;
        }
    }

    /**
     * Marks this copy as being {@link EditionCopyStatus#AVAILABLE available} after being returned.
     * @throws IllegalStateException Thrown when this copy is recorded as {@link EditionCopyStatus#LOST lost}.
     */
    public void markAvailable(){
        checkLost();
        if(status.isLoaned()){
            status = EditionCopyStatus.AVAILABLE;
        }
    }

    /**
     * Marks this copy as being lost i.e. a physical or digital copy no longer exists on
     * the library premises. This cannot be changed.
     */
    public void markLost(){
        if(!status.isLost()){
            status = EditionCopyStatus.LOST;
        }
    }

    private void checkLost(){
        if(status.isLost()){
            throw new IllegalStateException("This edition copy is lost. Its status cannot be changed.");
        }
    }
}
