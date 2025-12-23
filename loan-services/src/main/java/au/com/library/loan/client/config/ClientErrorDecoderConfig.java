package au.com.library.loan.client.config;

import au.com.library.loan.exception.ClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the use of the {@link ErrorDecoder Feign error decoder} to handle errors thrown
 * by requests to the book and member services.
 *
 * @see ClientErrorDecoder
 */
@Configuration
public class ClientErrorDecoderConfig {

    private final ClientErrorDecoder errorDecoder;

    public ClientErrorDecoderConfig(ClientErrorDecoder errorDecoder) {
        this.errorDecoder = errorDecoder;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return errorDecoder;
    }
}

