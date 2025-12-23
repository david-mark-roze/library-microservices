package au.com.library.loan.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the use of the Jackson databind {@link ObjectMapper} object to be used particularly
 * by the {@link au.com.library.loan.exception.ClientErrorDecoder Feign error decoder} that handles exceptions
 * thrown by requests to the book and member services.
 *
 * @see ObjectMapper
 * @see au.com.library.loan.exception.ClientErrorDecoder
 */
@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register module for Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        // Optional: serialize dates as ISO strings instead of timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
