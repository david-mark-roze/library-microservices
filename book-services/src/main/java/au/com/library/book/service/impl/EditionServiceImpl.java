package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionDTO;
import au.com.library.book.entity.Book;
import au.com.library.book.entity.Edition;
import au.com.library.book.repository.BookRespository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionService;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * The {@link EditionService} implementation.
 */
@Service
@AllArgsConstructor
public class EditionServiceImpl implements EditionService {

    private EditionRepository editionRepository;
    private BookRespository bookRespository;

    @Override
    @Transactional(readOnly = true)
    public EditionDTO findEdition(Long id) throws ResourceNotFoundException {
        return Mapper.map(findById(id), EditionDTO.class);
    }

    @Override
    public EditionDTO updateEdtion(Long id, EditionDTO editionDTO) throws ResourceNotFoundException {
        Edition edition = findById(id);

        edition.setIsbn(editionDTO.getIsbn());
        edition.setEdition(editionDTO.getEdition());
        edition.setFormat(editionDTO.getFormat());
        edition.setPublicationYear(editionDTO.getPublicationYear());
        edition.setPublisher(editionDTO.getPublisher());

        Edition saved = editionRepository.save(edition);
        return Mapper.map(saved, EditionDTO.class);
    }


    @Override
    public EditionDTO addEdition(Long bookId, EditionDTO editionDTO) throws ResourceNotFoundException {
        Book book = bookRespository.findById(bookId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("The book with the id %s could not be found", bookId)
                )
        );
        Edition edition = Mapper.map(editionDTO, Edition.class);
        edition.setBook(book);
        Edition saved = editionRepository.save(edition);
        return Mapper.map(saved, EditionDTO.class);
    }

    @Override
    public Collection<EditionDTO> findEditions(Long bookId) throws ResourceNotFoundException {
        if(!bookRespository.existsById(bookId)){
            throw new ResourceNotFoundException(String.format("The book with the id %s could not be found", bookId));
        }
        List<Edition> editions = editionRepository.findByBookId(bookId);
        return editions.stream().map(
                (e) -> Mapper.map(e, EditionDTO.class)
        ).toList();
    }

    private Edition findById(Long id){
        return editionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Edition with id %s could not be found", id)
                )
        );
    }
}
