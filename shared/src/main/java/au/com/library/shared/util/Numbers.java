package au.com.library.shared.util;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * Utility class for validating numbers, throwing exceptions if the validations fail.
 */
public class Numbers {

    /**
     * Validates that the provided number is not null and not zero. If the number is null or zero, it throws an exception provided by the exception supplier.
     *
     * @param number            The number to validate.
     * @param exceptionSupplier A supplier that provides the exception to be thrown if the validation fails.
     * @return The validated number if it is non-null and non-zero.
     * @throws RuntimeException If the number is null or zero, the exception provided by the supplier will be thrown.
     */
    public static Number isNonNullNonZeroOrElseThrow(Number number, Supplier <? extends RuntimeException> exceptionSupplier) {
        if (number == null) {
            throw exceptionSupplier.get();
        }

        switch (number){
                case Integer i when i == 0 -> throw exceptionSupplier.get();
                case Long l when l == 0L -> throw exceptionSupplier.get();
                case Double d when d == 0.0 -> throw exceptionSupplier.get();
                case Float f when f == 0.0f -> throw exceptionSupplier.get();
                case BigDecimal bd when bd.compareTo(BigDecimal.ZERO) == 0 -> throw exceptionSupplier.get();
                case Short s when s == 0 -> throw exceptionSupplier.get();
                case Byte b when b == 0 -> throw exceptionSupplier.get();
                default -> {
                    if (number.doubleValue() == 0) {
                        throw exceptionSupplier.get();
                    }
                }
        }
        return number;
    }

    /**
     * Validates that the provided number is not null and positive. If the number is null or not positive, it throws an exception provided by the exception supplier.
     *
     * @param number            The number to validate.
     * @param exceptionSupplier A supplier that provides the exception to be thrown if the validation fails.
     * @return The validated number if it is non-null and positive.
     * @throws RuntimeException If the number is null or not positive, the exception provided by the supplier will be thrown.
     */
    public static Number isNonNullPositiveOrElseThrow(Number number, Supplier <? extends RuntimeException> exceptionSupplier) {
        if (number == null) {
            throw exceptionSupplier.get();
        }
        switch (number){
            case Integer i when i <= 0 -> throw exceptionSupplier.get();
            case Long l when l <= 0L -> throw exceptionSupplier.get();
            case Double d when d <= 0.0 -> throw exceptionSupplier.get();
            case Float f when f <= 0.0f -> throw exceptionSupplier.get();
            case BigDecimal bd when bd.compareTo(BigDecimal.ZERO) <= 0 -> throw exceptionSupplier.get();
            case Short s when s <= 0 -> throw exceptionSupplier.get();
            case Byte b when b <= 0 -> throw exceptionSupplier.get();
            default -> {
                if (number.doubleValue() <= 0) {
                    throw exceptionSupplier.get();
                }
            }
        }
        return number;
    }

    static void main() {
        try {
            System.out.println(isNonNullNonZeroOrElseThrow(0, () -> new IllegalArgumentException("Number cannot be null or zero")));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        try {
            System.out.println(isNonNullPositiveOrElseThrow(-5, () -> new IllegalArgumentException("Number cannot be null or non-positive")));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
