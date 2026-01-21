package au.com.library.loan.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topic setup.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.loan-return}")
    private String loanReturn;

    /**
     * Creates a Kafka topic for loan returns.
     *
     * @return a NewTopic instance representing the loan return topic.
     */
    @Bean
    public NewTopic loanReturn() {
        return TopicBuilder.name(loanReturn).build();
    }
}
