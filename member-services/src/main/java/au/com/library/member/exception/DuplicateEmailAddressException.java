package au.com.library.member.exception;

public class DuplicateEmailAddressException extends RuntimeException {

    public DuplicateEmailAddressException(String message) {
        super(message);
    }
}
