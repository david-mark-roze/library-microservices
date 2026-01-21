package au.com.library.loan.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Publishes loan events to Kafka topics.
 */
@Component
@RequiredArgsConstructor
public class LoanPublisher {

    private final KafkaTemplate<String, Object> template;

    private final NewTopic loanReturnTopic;
    private final NewTopic loanCreateTopic;
    private final NewTopic loanLostTopic;

    /**
     * Publishes an event to the specified Kafka topic with the given key.
     *
     * @param topic the Kafka topic to publish to
     * @param key   the key for the message. Typically, the loan ID.
     * @param event the event payload
     */
    public void publish(NewTopic topic, String key, Object event) {
        var message = MessageBuilder.withPayload(event).
                setHeader(KafkaHeaders.TOPIC, topic.name()).
                setHeader(KafkaHeaders.KEY, key).
                build();
        template.send(message);
    }




}
