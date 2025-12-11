package au.com.library.shared.exception;

/**
 * Thrown when a requested resource could not be found.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
