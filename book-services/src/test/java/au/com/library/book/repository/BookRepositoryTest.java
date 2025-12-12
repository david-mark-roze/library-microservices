package au.com.library.book.repository;

import au.com.library.book.entity.Book;
import au.com.library.book.entity.BookFormat;
import au.com.library.book.entity.Edition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRespository bookRespository;
    @Autowired
    private EditionRepository editionRepository;

    @PersistenceContext
    private EntityManager entityManager;

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
        Book saved = bookRespository.save(book);
        Assertions.assertThat(saved).isNotNull();
    }

    /**
     * Tests the successful update of an existing {@link Book book}.
     */
    @Test
    @DisplayName("testUpdateBook")
    public void givenExistingBook_whenChangeMade_thenBookUpdated(){
        Book saved = bookRespository.save(book);

        saved.setAuthor("J.R.R. Tolkien");
        Book updated = bookRespository.save(saved);

        Assertions.assertThat(saved.getAuthor()).isEqualTo(updated.getAuthor());
    }

    /**
     * Tests that an existing book may be {@link BookRespository#findById(Object) found by its id}.
     */
    @Test
    @DisplayName("testFindBookById")
    public void givenExistingBook_whenFindingById_thenBookFound(){
        Book saved = bookRespository.save(book);
        Optional<Book> found = bookRespository.findById(saved.getId());
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
        Assertions.assertThatThrownBy(() -> bookRespository.save(book));
    }
    /**
     * Tests that an attempt to update an existing {@link Book book} that has had required fields
     * removed fails.
     */
    @Test
    @DisplayName("testBookUpdateFailsWhenRequiredFieldsMissing")
    public void givenExisitingBookWithMissingFields_whenUpdating_thenSaveFails(){
        Book saved = bookRespository.save(book);
        saved.setTitle(null);
        Assertions.assertThatThrownBy(() -> {
            bookRespository.save(book);
            // Forces the commit which should result in the error
            bookRespository.flush();
        });
    }

    /**
     * Tests that an {@link Edition edition} of a {@link Book book} is successfully saved and that its
     * parent {@link Book} object can be referenced by its {@link Edition#getBook()} method.
     */
    @Test
    @DisplayName("testCreateEdition")
    public void givenNewEdition_whenSaveEdition_thenEditionCreated(){
        Book savedBook = bookRespository.save(book);
        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");
        Edition savedEdition = editionRepository.save(edition);
        Assertions.assertThat(savedEdition).isNotNull();
        Assertions.assertThat(savedEdition.getBook()).isNotNull();
    }

    /**
     * Tests that an {@link Edition edition} can be updated successfully, maintaining its
     * reference to its parent {@link Book book}.
     */
    @Test
    @DisplayName("testUpdateEdition")
    public void givenExistingEditionUpdated_whenSaveEdition_thenEditionUpdated(){
        Book savedBook = bookRespository.save(book);
        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");
        Edition savedEdition = editionRepository.save(edition);

        savedEdition.setEdition("2nd Edition");
        Edition updatedEdition = editionRepository.save(savedEdition);

        Assertions.assertThat(updatedEdition).isNotNull();
        Assertions.assertThat(updatedEdition.getEdition()).isEqualTo(savedEdition.getEdition());
        Assertions.assertThat(updatedEdition.getBook()).isNotNull();
    }

    /**
     * This test tests the creation of many {@link Edition editions} of a {@link Book book}
     * and the subsequent fetching of that book, testing that
     * <ul>
     *     <li>The editions for that book are created successfully</li>
     *     <li>When the book is subsequently fetched, the associated editions are
     *     also fetched with the book.</li>
     * </ul>
     * This production like environment is simulated by {@link EntityManager#flush() flushing}
     * and {@link EntityManager#clear() clearing} the {@link EntityManager} before
     * {@link BookRespository#findById(Object) fetching the book}.
     */
    @Test
    @DisplayName("testCreateManyEditions")
    public void givenManyEditionsCreate_whenSavingAll_thenManyEditionsCreated(){
        // Step 1: Save the parent Book
        Book savedBook = bookRespository.save(book);
        Edition edition1 = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");

        // Step 2: Save the child editions
        Edition edition2 = buildTestEdition(
                savedBook,
                "ISBN-124",
                "2st Edition",
                BookFormat.HARDBACK,
                1950
                ,"Tolkien Publishing");
        Edition edition3 = buildTestEdition(
                savedBook,
                "ISBN-125",
                "3st Edition",
                BookFormat.HARDBACK,
                1955
                ,"Tolkien Publishing");

        List<Edition> toSave = List.of(edition1, edition2, edition3);
        List<Edition> saved = editionRepository.saveAll(toSave);

        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.size()).isEqualTo(toSave.size());

        // Step 3: Flush + clear to simulate a new request in production
        // before fetching the saved book
        entityManager.flush();
        entityManager.clear();

        // Step 4: Load using a fresh persistence context to
        // simulate the state of a Book in production.
        Book foundBook = bookRespository.findById(savedBook.getId()).orElseThrow(() -> new IllegalStateException("Book not found!"));

        // The book's editions should be present
        Assertions.assertThat(foundBook.getEditions()).isNotNull();
        Edition selectedEdition = foundBook.getEditions().stream().findFirst().get();
        // Checked that the book's editions have a reference to the book by
        // checking the first one in the List
        Assertions.assertThat(selectedEdition.getBook()).isNotNull();
    }


    /**
     * Tests that an existing edition can be retrieved successfully.
     */
    @Test
    @DisplayName("testFindEditionById")
    public void givenExisitngEdition_whenFindById_thenEditionFound(){

        Book savedBook = bookRespository.save(book);
        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");

        Edition saved = editionRepository.save(edition);
        Edition found = editionRepository.findById(
                saved.getId()).orElseThrow(
                () -> new IllegalStateException("Edition not found")
        );
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.getBook()).isNotNull();
    }

    private Edition buildTestEdition(Book book, String isbn, String edition, BookFormat format, int year, String publisher){
        return Edition.builder().
                isbn(isbn).
                edition(edition).
                format(format).
                publicationYear(year).
                publisher(publisher).
                book(book).build();
    }
}
