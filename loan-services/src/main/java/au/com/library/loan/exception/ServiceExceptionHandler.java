package au.com.library.loan.exception;

import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ConflictException;
import au.com.library.shared.exception.ErrorDetails;
import au.com.library.shared.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles a {@link CopyUnavailableException} which is thrown when a request edition copy to borrow was not marked as {@link au.com.library.loan.dto.EditionCopyStatus#AVAILABLE available}.
     *
     * @param exception A reference to the {@link CopyUnavailableException} object.
     * @param request   The {@link WebRequest} object.
     * @return The {@link ResponseEntity} object containing the error details.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleCopyUnavailableException(CopyUnavailableException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "COPY_UNAVAILABLE");
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    /**
     * Handles a {@link ResourceNotFoundException}.
     * @param exception A reference to the {@link ResourceNotFoundException} object.
     * @param request The {@link WebRequest} object.
     * @return The {@link ResponseEntity} object containing the error details.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "NOT_FOUND");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles a {@link BadRequestException}.
     * @param exception A reference to the {@link BadRequestException} object.
     * @param request The {@link WebRequest} object.
     * @return The {@link ResponseEntity} object containing the error details.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false),
                "BAD_REQUEST");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles a {@link ConflictException}.
     * @param exception A reference to the {@link ConflictException} object.
     * @param request The {@link WebRequest} object.
     * @return The {@link ResponseEntity} object containing the error details.
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleConflictException(ConflictException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(
                LocalDateTime.now(),
                exception.getMessage(),
                request.getDescription(false),
                "CONFLICT");
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }
}
