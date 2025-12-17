package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.dto.EditionCopyStatusDTO;
import au.com.library.book.entity.Edition;
import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.book.repository.EditionCopyRepository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionCopyService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.BarcodeGenerator;
import au.com.library.shared.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
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
        return Mapper.map(saved, EditionCopyDTO.class);
    }

    @Override
    public EditionCopyDTO updateCopyStatus(Long editionId, Long copyId, EditionCopyStatusDTO statusDTO) {
        EditionCopy copy = editionCopyRepository.findByIdAndEditionId(editionId, copyId).orElseThrow(
                ()-> new ResourceNotFoundException(String.format("The edition copy with the copy id %s and edition id %s could not be found", copyId, editionId)
                )
        );
        // If the copy is recorded as 'lost' its status cannot be changed. it must be replaced.
        if(copy.getStatus().isLost()){
            throw new BadRequestException("A the status of a lost edition copy cannot be changed.");
        }
        // If the status has not changed, simply return the EditionCopyDTO with existing details.
        if(copy.getStatus().equals(statusDTO.getStatus())){
            LOGGER.info("The edition copy status is unchanged. No update will occur.");
            return Mapper.map(copy, EditionCopyDTO.class);
        }
        copy.setStatus(statusDTO.getStatus());
        EditionCopy saved = editionCopyRepository.save(copy);
        return Mapper.map(saved, EditionCopyDTO.class);
    }

    @Override
    public EditionCopyDTO findCopy(Long editionId, Long copyId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException {
        List<EditionCopy> copies = editionCopyRepository.findByEditionId(editionId);
        return copies.stream().map(
                copy -> Mapper.map(copy, EditionCopyDTO.class)
        ).toList();
    }
}
