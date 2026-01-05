package au.com.library.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * This contains the attributes associated with a particular edition of a {@link Book book}, such as its
 * {@link #getIsbn() ISBN}, {@link #getPublisher() publisher} and {@link #getPublicationYear() year published}.
 * The library may have several {@link #getCopies() copies} of the same edition.
 *
 * @see EditionCopy
 * @see Book
 *
 * @author David Roze
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Edition {

    private static final int LENGTH_ISBN = 20;
    private static final int LENGTH_EDITION = 50;
    private static final int LENGTH_FORMAT = 50;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = LENGTH_ISBN)
    private String isbn;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Integer publicationYear;

    @Column(nullable = false, length = LENGTH_EDITION)
    private String edition;

    // Bidirectional many to one - This child Edition references its parent Book via
    // the 'book_id' foreign key. In the parent, this is the 'id' column.
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Book book;

    @Column(nullable = false, length = LENGTH_FORMAT)
    @Enumerated(EnumType.STRING)
    private BookFormat format;

    // Bidirectional one-to-many where the child ElementCopy objects reference the parent
    // Edition by the name 'edition'.
    @OneToMany(mappedBy = "edition", fetch = FetchType.LAZY)
    private Set<EditionCopy> copies = new HashSet<>();
}
