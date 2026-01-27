package au.com.library.book.kafka.error;

import au.com.library.book.exception.InvalidEditionCopyStatusException;
import au.com.library.book.kafka.exception.UnknownLoanEventTypeException;
import com.fasterxml.jackson.core.JsonParseException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.util.backoff.FixedBackOff;

/**
 * Configuration for Kafka error handling, specifically for routing failed messages to a Dead Letter Topic (DLT).
 */
@Configuration
public class ErrorHandlingConfig {

    private static final String DLT_SUFFIX = ".DLT";

    /**
     * Creates a DefaultErrorHandler that routes failed messages to a Dead Letter Topic (DLT).
     *
     * @param kafkaTemplate the KafkaTemplate used to publish messages to Kafka topics.
     * @return a configured DefaultErrorHandler instance.
     */
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {

        DefaultErrorHandler errorHandler = getDefaultErrorHandler(kafkaTemplate);
        // Specify exceptions that should not be retried and sent directly to the DLT.
        errorHandler.addNotRetryableExceptions(
                UnknownLoanEventTypeException.class,
                InvalidEditionCopyStatusException.class,
                DeserializationException.class,
                SerializationException.class,
                JsonParseException.class,
                IllegalArgumentException.class);

        return errorHandler;
    }

    private DefaultErrorHandler getDefaultErrorHandler(KafkaTemplate<Object, Object> kafkaTemplate) {
        // Configure a DeadLetterPublishingRecoverer to send failed messages to a DLT.
        // The ConsumerRecord ('record') contains the topic and partition information. Here, we
        // append ".DLT" to the original topic name to create the DLT topic.
        var recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate, (record, _) -> new TopicPartition(
                record.topic() + DLT_SUFFIX,
                record.partition()
        ));
        // Configure a FixedBackOff policy with no delay and no retries.
        // The interval is set to 0 milliseconds, and the maxAttempts is set to 0 to avoid retries.
        // it is called backoff because it controls the back off time between retries which is 0 here.
        var backoff = new FixedBackOff(0, 0);

        DefaultErrorHandler errorHandler = new DefaultErrorHandler(recoverer, backoff);
        return errorHandler;
    }
}
