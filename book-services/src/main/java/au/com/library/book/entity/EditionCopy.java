package au.com.library.book.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EditionCopy {

    private static final int LENGTH_STATUS = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String barcode;

    @Column(nullable = false, length = LENGTH_STATUS)
    @Enumerated(EnumType.STRING)
    private EditionCopyStatus status;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime dateAquired;

    // Bi-directional many to one - This child EditionCopy references its parent Edition via
    // the 'edition_id' foreign key. In the parent, this is the 'id' column.
    @JoinColumn(name = "edition_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Edition edition;

}
