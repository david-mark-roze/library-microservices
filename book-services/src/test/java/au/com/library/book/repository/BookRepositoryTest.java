package au.com.library.book.repository;

import au.com.library.book.entity.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRespository repository;

    private Book book;

    /**
     * Sets up each test by creating a {@link Book} object and
     * initialising the <code>book</code> instance variable.
     */
    @BeforeEach
    void setup(){
        book = Book.builder().
                title("The Fellowship of the Ring").
                author("Tolkien, J.R.R").build();

    }

    /**
     * Tests the successful creation of a {@link Book book}.
     */
    @Test
    @DisplayName("testCreateBook")
    public void givenNewBook_whenBookSaved_thenBookCreated(){
        Book saved = repository.save(book);
        Assertions.assertThat(saved).isNotNull();
    }

    /**
     * Tests the successful update of an existing {@link Book book}.
     */
    @Test
    @DisplayName("testUpdateBook")
    public void givenExistingBook_whenChangeMade_thenBookUpdated(){
        Book saved = repository.save(book);

        saved.setAuthor("J.R.R. Tolkien");
        Book updated = repository.save(saved);

        Assertions.assertThat(saved.getAuthor()).isEqualTo(updated.getAuthor());
    }

    /**
     * Tests that an existing book may be {@link BookRespository#findById(Object) found by its id}.
     */
    @Test
    @DisplayName("testFindBookById")
    public void givenExistingBook_whenFindingById_thenBookFound(){
        Book saved = repository.save(book);
        Optional<Book> found = repository.findById(saved.getId());
        if(found.isPresent()){
            Assertions.assertThat(found.get()).isNotNull();
        } else {
            Assertions.fail("Book not found");
        }
    }

    /**
     * Tests that an attempt to create a new {@link Book book} when required fields are missing
     * fails.
     */
    @Test
    @DisplayName("testBookCreateFailsWhenRequiredFieldsMissing")
    public void givenEmptyBook_whenSaving_thenSaveFails(){
        Book book = Book.builder().build();
        Assertions.assertThatThrownBy(() -> repository.save(book));
    }
    /**
     * Tests that an attempt to update an existing {@link Book book} that has had required fields
     * removed fails.
     */
    @Test
    @DisplayName("testBookUpdateFailsWhenRequiredFieldsMissing")
    public void givenExisitingBookWithMissingFields_whenUpdating_thenSaveFails(){
        Book saved = repository.save(book);
        saved.setTitle(null);
        Assertions.assertThatThrownBy(() -> {
            repository.save(book);
            // Forces the commit which should result in the error
            repository.flush();
        });
    }

    public void given_when_then(){

    }
}
