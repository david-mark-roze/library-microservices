package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.entity.Edition;
import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.book.repository.EditionCopyRepository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionCopyService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.BarcodeGenerator;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class EditionCopyServiceImpl implements EditionCopyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditionCopyServiceImpl.class);

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
        return EditionCopyDTO.toDTO(saved);
    }

    @Override
    public EditionCopyDTO borrowCopy(Long editionId, Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findByIdAndEditionId(copyId, editionId);
        copy.markBorrowed();
        EditionCopy saved = editionCopyRepository.save(copy);
        return EditionCopyDTO.toDTO(saved);
    }

    @Override
    public EditionCopyDTO returnCopy(Long editionId, Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findByIdAndEditionId(copyId, editionId);
        copy.markAvailable();
        EditionCopy saved = editionCopyRepository.save(copy);
        return EditionCopyDTO.toDTO(saved);
    }

    @Override
    public EditionCopyDTO markCopyLost(Long editionId, Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findByIdAndEditionId(copyId, editionId);
        copy.markLost();
        EditionCopy saved = editionCopyRepository.save(copy);
        return EditionCopyDTO.toDTO(saved);
    }

    @Override
    public EditionCopyDTO findCopy(Long editionId, Long copyId) throws ResourceNotFoundException {
        return EditionCopyDTO.toDTO(findByIdAndEditionId(copyId, editionId));
    }

    @Override
    public List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException {
        List<EditionCopy> copies = editionCopyRepository.findByEditionId(editionId);
        return copies.stream().map(EditionCopyDTO::toDTO).toList();
    }

    private EditionCopy findByIdAndEditionId(Long copyId, Long editionId){
        if(editionId == null){
            return editionCopyRepository.findById(copyId).orElseThrow(
                    ()-> new ResourceNotFoundException(String.format("The edition copy with the copy id %s could not be found", copyId)
                    )
            );
        }
        return editionCopyRepository.findByIdAndEditionId(copyId, editionId).orElseThrow(
                ()-> new ResourceNotFoundException(String.format("The edition copy with the copy id %s and edition id %s could not be found", copyId, editionId)
                )
        );
    }
}
