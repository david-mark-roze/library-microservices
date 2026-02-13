package au.com.library.loan.util;

import au.com.library.shared.util.Numbers;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Utility class for validating method parameters and ensuring they meet certain criteria,   throwing
 * appropriate exceptions when validation fails.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ValidationUtil {

    /**
     * Checks if the provided object is null. If it is null, it throws an {@link IllegalArgumentException} with the provided message.
     * @param object The object to check for nullity.
     * @param message The message to be included in the exception if the object is null.
     * @return The provided object if it is not null.
     * @param <T> The type of the object being checked.
     */
    public static  <T> T checkNonNullParameter(T object, String message){
        return Objects.requireNonNullElseGet(object, () -> {
            throw new IllegalArgumentException(message);
        });
    }

    /**
     * Checks if the provided number is non-null and positive. If the number is null or not positive, it throws an {@link IllegalArgumentException} with the provided message.
     *
     * @param number  The number to check for validity.
     * @param message The message to be included in the exception if the number is invalid.
     * @return The provided number if it is non-null and positive.
     */
    public static Number checkNonNullPositiveNumber(Number number, String message) {
        return Numbers.isNonNullPositiveOrElseThrow(number, () -> new IllegalArgumentException(message));
    }
}
