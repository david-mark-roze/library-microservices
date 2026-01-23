package au.com.library.loan.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Kafka topic setup.
 */
@Configuration
public class LoanTopicConfig {

    @Value("${spring.kafka.topic.loan-event}")
    private String loanEventTopic;

    /**
     * Creates a Kafka topic for loan events.
     *
     * @return a NewTopic instance representing the loan event topic.
     */
    @Bean
    public NewTopic loanEventTopic() {
        return TopicBuilder.name(loanEventTopic).build();
    }

    @PostConstruct
    void logTopic() {
        System.out.println("Loan event topic = " + loanEventTopic);
    }
}
