package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionDTO;
import au.com.library.book.entity.Edition;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionService;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@link EditionService} implementation.
 */
@Service
@AllArgsConstructor
public class EditionServiceImpl implements EditionService {

    private EditionRepository repository;

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

        Edition saved = repository.save(edition);
        return Mapper.map(saved, EditionDTO.class);
    }

    private Edition findById(Long id){
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Edition with id %s could not be found", id)
                )
        );
    }
}
