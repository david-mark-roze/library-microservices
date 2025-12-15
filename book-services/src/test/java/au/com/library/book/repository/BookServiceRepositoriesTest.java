package au.com.library.book.repository;

import au.com.library.book.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookServiceRepositoriesTest {

    @Autowired
    private BookRespository bookRespository;
    @Autowired
    private EditionRepository editionRepository;
    @Autowired
    private EditionCopyRepository editionCopyRepository;

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
     * Tests the successful updateBook of an existing {@link Book book}.
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
     * Tests that an attempt to updateBook an existing {@link Book book} that has had required fields
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
    public void givenManyNewEditions_whenSavingAll_thenManyEditionsCreated(){
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
        forceCommit();

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
    public void givenExisitngEdition_whenFindingById_thenEditionFound(){

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
    /**
     * Tests all editions associated with a book can be retrieved successfully
     * for that book.
     */
    @Test
    @DisplayName("testFindEditionsByBookId")
    public void givenExisitingEditions_whenFindingEditionsByBookId_thenEditionsFound(){

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
        forceCommit();

        // Step 4: Load the editions for the book using a fresh persistence context to
        // simulate the state of a editions in production.
        List<Edition> editions = editionRepository.findByBookId(savedBook.getId());

        // The editions should be present
        Assertions.assertThat(editions).isNotNull();
        Edition selectedEdition = editions.stream().findFirst().get();
        // Checked that the editions have a reference to the book by
        // checking the first one in the List
        Assertions.assertThat(selectedEdition.getBook()).isNotNull();
    }

    /**
     * Tests that an {@link EditionCopy edition copy} is created successfully.
     */
    @Test
    @DisplayName("testCreateEditionCopy")
    public void givenNewCopy_whenSavingCopy_thenCopyIsCreated(){
        Book savedBook = bookRespository.save(book);

        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");
        Edition savedEdition = editionRepository.save(edition);

        EditionCopy copy = buildTestEditionCopy(edition, "barcode");
        EditionCopy savedCopy = editionCopyRepository.save(copy);

        Assertions.assertThat(savedCopy).isNotNull();
        Assertions.assertThat(savedCopy.getEdition()).isNotNull();
        Assertions.assertThat(savedCopy.getEdition().getBook()).isNotNull();
    }
    /**
     * Tests that an existing {@link EditionCopy edition copy} is updated successfully.
     */
    @Test
    @DisplayName("testUpdateEditionCopy")
    public void givenExistingCopy_whenSavingCopy_thenCopyIsUpdated(){
        Book savedBook = bookRespository.save(book);

        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");
        Edition savedEdition = editionRepository.save(edition);

        EditionCopy copy = buildTestEditionCopy(edition, "barcode");
        EditionCopy savedCopy = editionCopyRepository.save(copy);

        savedCopy.setStatus(EditionCopyStatus.LOANED);
        EditionCopy updatedCopy = editionCopyRepository.save(copy);

        Assertions.assertThat(updatedCopy.getStatus()).isEqualTo(savedCopy.getStatus());
    }

    /**
     * This test tests the creation of many {@link EditionCopy edition copiess} of an {@link Edition edition}
     * and the subsequent fetching of that edition, testing that
     * <ul>
     *     <li>The copies for that edition are created successfully</li>
     *     <li>When the edition is subsequently fetched, the associated copies
     *     are also fetched with the edition.</li>
     * </ul>
     * This production like environment is simulated by {@link EntityManager#flush() flushing}
     * and {@link EntityManager#clear() clearing} the {@link EntityManager} before
     * {@link EditionRepository#findById(Object) fetching the edition}.
     */
    @Test
    @DisplayName("testCreateManyEditionCopies")
    public void givenManyNewEditionCopies_whenSavingAll_thenManyEditionCopiesCreated(){
        // Step 1: Save the parent Book
        Book savedBook = bookRespository.save(book);

        // Step 2: Create and save the child edition of the book which is the parent edition
        // of the new copies
        Edition edition = buildTestEdition(
                savedBook,
                "ISBN-123",
                "1st Edition",
                BookFormat.HARDBACK,
                1943
                ,"Tolkien Publishing");
        Edition savedEdition = editionRepository.save(edition);

        // Step 3: Create and save the new edition copies
        EditionCopy copy1 = buildTestEditionCopy(edition, "barcode1");
        EditionCopy copy2 = buildTestEditionCopy(edition, "barcode2");
        EditionCopy copy3 = buildTestEditionCopy(edition, "barcode3");

        List<EditionCopy> toSave = List.of(copy1, copy2, copy3);
        List<EditionCopy> savedCopies = editionCopyRepository.saveAll(toSave);

        // Step 4: Test the 'save all' results look as expected
        Assertions.assertThat(savedCopies).isNotNull();
        Assertions.assertThat(savedCopies.size()).isEqualTo(toSave.size());

        // Step 5: Flush + clear to simulate a new request in production
        // before fetching the saved edition to check for the presence of
        // its children and related entities.
        forceCommit();

        // Fetch the addition post flush and clear to check the validity of it, its
        // related copies and other related entities.
        Edition foundEdition = editionRepository.findById(
                edition.getId()).orElseThrow(
                        () -> new IllegalStateException("Edition not found")
        );
        Assertions.assertThat(foundEdition.getCopies()).isNotNull();
        Assertions.assertThat(foundEdition.getCopies().size()).isEqualTo(savedCopies.size());
        EditionCopy selectedCopy = foundEdition.getCopies().stream().findFirst().get();
        Assertions.assertThat(selectedCopy.getEdition()).isNotNull();
        Assertions.assertThat(selectedCopy.getEdition().getId()).isEqualTo(savedEdition.getId());
        Assertions.assertThat(selectedCopy.getEdition().getBook()).isNotNull();
        Assertions.assertThat(selectedCopy.getEdition().getBook().getId()).isEqualTo(savedBook.getId());
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

    private EditionCopy buildTestEditionCopy(Edition edition, String barcode){
        return EditionCopy.builder().
                barcode(barcode).
                edition(edition).
                status(EditionCopyStatus.AVAILABLE).
                build();
    }

    private void forceCommit(){
        entityManager.flush();
        entityManager.clear();
    }
}
