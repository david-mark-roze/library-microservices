package au.com.library.loan.config;

import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for loan management, including loan period, hold period, and renewal limits.
 * This class is annotated with @ConfigurationProperties to bind properties prefixed with "loan" from the application configuration,
 * and @Validated to ensure that the properties meet the specified validation constraints.
 *
 * @param periodDays The number of days for the loan period. Must be a positive integer.
 * @param holdPeriodDays The number of days for the hold allocation before it will expire. Must be a positive integer.
 * @param renewalLimit The maximum number of times a loan can be renewed. Must be a positive integer.
 */
@ConfigurationProperties(prefix = "loan")
@Validated
public record LoanConfiguration(
        @Positive int periodDays,
        @Positive int holdPeriodDays,
        @Positive int renewalLimit
) {
}
