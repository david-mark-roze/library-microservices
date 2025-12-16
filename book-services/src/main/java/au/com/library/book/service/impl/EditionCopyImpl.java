package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.entity.Edition;
import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.book.repository.EditionCopyRepository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionCopyService;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.BarcodeGenerator;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Service
public class EditionCopyImpl implements EditionCopyService {

    private EditionRepository editionRepository;
    private EditionCopyRepository editionCopyRepository;

    @Override
    public EditionCopyDTO addCopy(Long editionId) throws ResourceNotFoundException {
        Edition edition = editionRepository.findById(editionId).orElseThrow(
                ()-> new ResourceNotFoundException("An edition with the id %s could not be found")
        );
        EditionCopy copy = EditionCopy.builder().
                edition(edition).
                status(EditionCopyStatus.AVAILABLE).
                barcode(BarcodeGenerator.generate()).
                build();
        EditionCopy saved = editionCopyRepository.save(copy);
        return Mapper.map(saved, EditionCopyDTO.class);
    }

    @Override
    public EditionCopyDTO updateCopy(Long copyId, EditionCopyDTO copyDTO) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public EditionCopyDTO findCopy(Long copyId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException {
        return List.of();
    }
}
