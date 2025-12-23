package au.com.library.loan.exception;

import au.com.library.shared.exception.BadRequestException;
import au.com.library.shared.exception.ErrorDetails;
import au.com.library.shared.exception.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class ClientErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientErrorDecoder.class);

    private static final int NOT_FOUND = 404;
    private static final int BAD_REQUEST = 400;

    private final ObjectMapper mapper;

    public ClientErrorDecoder(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            Response.Body body = response.body();
            if(body == null){
                return FeignException.errorStatus(methodKey, response);
            }
            ErrorDetails error =
                    mapper.readValue(body.asInputStream(), ErrorDetails.class);
            if(response.status() == HttpStatus.NOT_FOUND.value()){
                return new ResourceNotFoundException(error.getMessage());
            } else if(response.status() == HttpStatus.BAD_REQUEST.value()){
                return new BadRequestException(error.getMessage());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return FeignException.errorStatus(methodKey, response);
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
