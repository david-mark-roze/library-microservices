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

    @Value("${spring.kafka.topic.loan-create}")
    private String loanCreateTopic;

    @Value("${spring.kafka.topic.loan-return}")
    private String loanReturnTopic;

    @Value("${spring.kafka.topic.loan-lost}")
    private String loanLostTopic;

    /**
     * Creates a Kafka topic for loan returns.
     *
     * @return a NewTopic instance representing the loan return topic.
     */
    @Bean
    public NewTopic loanReturnTopic() {
        return TopicBuilder.name(loanReturnTopic).build();
    }

    /**
     * Creates a Kafka topic for loan creation.
     *
     * @return a NewTopic instance representing the loan creation topic.
     */
    @Bean
    public NewTopic loanCreateTopic() {
        return TopicBuilder.name(loanCreateTopic).build();
    }

    /**
     * Creates a Kafka topic for lost loans.
     *
     * @return a NewTopic instance representing the loan lost topic.
     */
    @Bean
    public NewTopic loanLostTopic() {
        return TopicBuilder.name(loanLostTopic).build();
    }
}
