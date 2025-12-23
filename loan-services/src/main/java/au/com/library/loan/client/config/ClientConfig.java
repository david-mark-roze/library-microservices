package au.com.library.loan.client.config;

import au.com.library.loan.exception.ClientErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {

    private final ClientErrorDecoder errorDecoder;

    public ClientConfig(ClientErrorDecoder errorDecoder) {
        this.errorDecoder = errorDecoder;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return errorDecoder;
    }
}

