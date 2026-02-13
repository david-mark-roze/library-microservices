package au.com.library.book.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This contains the basic attributes of a {@link Book book} i.e. the book's {@link #getTitle() title} and {@link #getAuthor() author}.
 * A book may have been published as many {@link #getEditions() editions}.
 *
 * @see Edition
 *
 * @author David Roze
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    // Bidirectional one-to-many where the child Element objects reference the parent
    // Book by the name 'book'.
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<Edition> editions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
