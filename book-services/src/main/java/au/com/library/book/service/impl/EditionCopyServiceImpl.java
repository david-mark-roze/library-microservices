package au.com.library.book.service.impl;

import au.com.library.book.dto.EditionCopyDTO;
import au.com.library.book.entity.Edition;
import au.com.library.book.entity.EditionCopy;
import au.com.library.book.entity.EditionCopyStatus;
import au.com.library.book.exception.InvalidEditionCopyStatusException;
import au.com.library.book.repository.EditionCopyRepository;
import au.com.library.book.repository.EditionRepository;
import au.com.library.book.service.EditionCopyService;
import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ResourceNotFoundException;
import au.com.library.shared.util.BarcodeGenerator;
import au.com.library.shared.util.Numbers;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class EditionCopyServiceImpl implements EditionCopyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditionCopyServiceImpl.class);

    private EditionRepository editionRepository;
    private EditionCopyRepository editionCopyRepository;

    @Override
    public EditionCopyDTO addCopy(Long editionId) throws ResourceNotFoundException, BadRequestException {
        Numbers.isNonNullNonZeroOrElseThrow(editionId, () -> new BadRequestException("The edition id must be a non null positive number."));
        Edition edition = findEditionById(editionId);
        EditionCopy copy = EditionCopy.of(edition);
        EditionCopy saved = editionCopyRepository.save(copy);
        return EditionCopyDTO.toDTO(saved);
    }

    @Override
    public List<EditionCopyDTO> addCopies(Long editionId, Integer numberOfCopies) throws ResourceNotFoundException, BadRequestException {
        Numbers.isNonNullNonZeroOrElseThrow(editionId, () -> new BadRequestException("The edition id must be a non null positive number."));
        Numbers.isNonNullNonZeroOrElseThrow(numberOfCopies, () -> new BadRequestException("The number of copies must be a non null positive number."));
        Edition edition = findEditionById(editionId);
        Collection<EditionCopy> copies = EditionCopy.of(edition, numberOfCopies);
        List<EditionCopy> saved = editionCopyRepository.saveAll(copies);
        return saved.stream().map(EditionCopyDTO::toDTO).toList();
    }

    @Override
    public void borrowCopy(Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findById(copyId);
        EditionCopyStatus status = copy.getStatus();
        switch (status){
            case AVAILABLE -> {
                copy.markBorrowed();
                editionCopyRepository.save(copy);
            }
            case LOANED -> LOGGER.info("The edition copy with id %s is already on loan", copyId);
            case LOST -> LOGGER.info("The edition copy with id %s is marked as lost and cannot be borrowed", copyId);
            default -> throw new InvalidEditionCopyStatusException("The edition copy status is invalid");
        }
    }

    @Override
    public void returnCopy(Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findById(copyId);
        EditionCopyStatus status = copy.getStatus();
        switch (status){
            case LOANED -> {
                copy.markAvailable();
                editionCopyRepository.save(copy);
            }
            case AVAILABLE -> LOGGER.info("The edition copy with id %s is already available in the library", copyId);
            case LOST -> LOGGER.info("The edition copy with id %s is marked as lost and cannot be returned", copyId);
            default -> throw new InvalidEditionCopyStatusException("The edition copy status is invalid");
        }
    }

    @Override
    public void markCopyLost(Long copyId) throws ResourceNotFoundException {
        EditionCopy copy = findById(copyId);
        EditionCopyStatus status = copy.getStatus();
        switch (status){
            case LOST -> LOGGER.info("The edition copy with id %s is already marked as lost", copyId);
            case AVAILABLE, LOANED -> {
                copy.markLost();
                editionCopyRepository.save(copy);
            }
            default -> throw new InvalidEditionCopyStatusException("The edition copy status is invalid");
        }
    }

    @Override
    public EditionCopyDTO findCopy(Long copyId) throws ResourceNotFoundException {
        return EditionCopyDTO.toDTO(findById(copyId));
    }

    @Override
    public List<EditionCopyDTO> findCopies(Long editionId) throws ResourceNotFoundException {
        List<EditionCopy> copies = editionCopyRepository.findByEditionId(editionId);
        return copies.stream().map(EditionCopyDTO::toDTO).toList();
    }

    private EditionCopy findById(Long copyId) {
        return editionCopyRepository.findById(copyId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("The edition copy with the copy id %s could not be found", copyId)
                )
        );
    }

    private Edition findEditionById(Long editionId){
        return editionRepository.findById(editionId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("The edition with the id %s could not be found", editionId)
                )
        );
    }
}
