package au.com.library.book.entity;

import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.util.BarcodeGenerator;
import au.com.library.shared.util.Numbers;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This represents a copy of an {@link Edition book edition} at the library
 * of which there may be more than one.
 *
 * @see Edition
 */
@Getter
@NoArgsConstructor
// The @EntityListeners annotation specifies that the AuditingEntityListener should be used
// to automatically populate auditing fields such as status when the copy has been loaned, returned, or marked as lost.
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
    // the 'edition_id' foreign key. In the parent, this is the 'id' column.
    @JoinColumn(name = "edition_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Edition edition;

    /**
     * Factory method to create a new edition copy with the given edition and an initial status of {@link EditionCopyStatus#AVAILABLE available}.
     *
     * @param edition The edition to which this copy belongs.
     * @return A new edition copy associated with the given edition.
     * @throws NullPointerException Thrown when the given edition is null.
     */
    public static EditionCopy of(Edition edition){
        return new EditionCopy(edition);
    }

    /**
     * Factory method to create a specified number of new {@link EditionCopyStatus#AVAILABLE available} edition copies.
     *
     * @param edition The edition to which these copies belong.
     * @param numberOfCopies The number of copies to be created.
     * @return A collection of new edition copies associated with the given edition.
     * @throws NullPointerException Thrown when the given edition is null.
     * @throws IllegalArgumentException Thrown when the number of copies to be created is less than or equal to zero.
     */
    public static Collection<EditionCopy> of(Edition edition, int numberOfCopies){
        Objects.requireNonNull(edition, "Edition copies must be associated with an edition.");
        if(numberOfCopies <= 0){
            throw new IllegalArgumentException("The number of edition copies to be created must be greater than zero. Provided value: " + numberOfCopies);
        }
        List<EditionCopy> copies = new ArrayList<>();
        for(int i = 0; i < numberOfCopies; i++){
            copies.add(of(edition));
        }
        return copies;
    }
    /**
     * Constructs a new edition copy with the given edition and an initial status of {@link EditionCopyStatus#AVAILABLE available}.
     *
     * @param edition The edition to which this copy belongs.
     * @throws NullPointerException Thrown when the given edition is null.
     */
    private EditionCopy(Edition edition) {
        Objects.requireNonNull(edition, "An edition copy must be associated with an edition.");
        this.edition = edition;
        this.status = EditionCopyStatus.AVAILABLE;
        this.barcode = BarcodeGenerator.generate();
    }

    /**
     * Marks this copy as being {@link EditionCopyStatus#LOANED on loan}, if it is not already.
     *
     * @throws ConflictException Thrown when this copy is recorded as {@link EditionCopyStatus#LOST lost} or already on loan.
     */
    public void markBorrowed(){
        checkLost();
        if(status.isLoaned()){
            throw new ConflictException("This edition copy is already on loan.");
        }
        status = EditionCopyStatus.LOANED;
    }

    /**
     * Marks this copy as being {@link EditionCopyStatus#AVAILABLE available}, typically after being returned from a loan.
     *
     * @throws ConflictException Thrown when this copy is recorded as {@link EditionCopyStatus#LOST lost} or already available.
     */
    public void markAvailable(){
        checkLost();
        if(status.isAvailable()){
            throw new ConflictException("This edition copy is already available.");
        }
        status = EditionCopyStatus.AVAILABLE;
    }

    /**
     * Marks this copy as being lost i.e. a physical or digital copy no longer exists on
     * the library premises. Once marked as lost, the status of this copy cannot be changed.
     *
     * @throws ConflictException Thrown when this copy is already recorded as {@link EditionCopyStatus#LOST lost}.
     */
    public void markLost(){
        if(status.isLost()){
            throw new ConflictException("The edition copy is already marked as lost.");
        }
        status = EditionCopyStatus.LOST;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        EditionCopy that = (EditionCopy) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EditionCopy{" +
                "id=" + id +
                ", barcode='" + barcode + '\'' +
                ", status=" + status +
                ", dateAcquired=" + dateAcquired +
                '}';
    }

    private void checkLost(){
        if(status.isLost()){
            throw new ConflictException("This edition copy is lost. Its status cannot be changed.");
        }
    }
}
