package au.com.library.member.exception;

import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ErrorDetails;
import au.com.library.shared.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Handles exceptions thrown by {@link au.com.library.member.service.MemberService} by returning a
 * {@link ResponseEntity} object from REST API request containing the {@link ErrorDetails error details}.
 */
@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleDuplicateEmailAddressException(DuplicateEmailAddressException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "DUPLICATE_EMAIL_ADDRESS");
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
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "RESOURCE_NOT_FOUND");
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException exception, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), exception.getMessage(), request.getDescription(false), "BAD_REQUEST");
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
