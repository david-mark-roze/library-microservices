package au.com.library.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A class that may be used by a microservice to record the details of an exception or error that may have occurred.
 * It may be populated by an exception handler and returned by a REST API request when an exception is thrown.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorDetails {

    /**
     * The date/time the error occurred.
     */
    private LocalDateTime timestamp;
    /**
     * The error message.
     */
    private String message;
    /**
     * The request path.
     */
    private String path;
    /**
     * The related error code.
     */
    private String errorCode;
}
