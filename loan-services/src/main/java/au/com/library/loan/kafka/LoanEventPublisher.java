package au.com.library.loan.kafka;

import au.com.library.loan.kafka.event.LoanCreatedEvent;
import au.com.library.loan.kafka.event.LoanEventType;
import au.com.library.loan.kafka.event.LoanLostEvent;
import au.com.library.loan.kafka.event.LoanReturnedEvent;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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
public class LoanEventPublisher {

    private final KafkaTemplate<String, Object> template;

    private final NewTopic loanReturnTopic;
    private final NewTopic loanCreateTopic;
    private final NewTopic loanLostTopic;

    /**
     * Publishes a {@link LoanReturnedEvent loan returned event} to the loan return topic.
     * @param key The message key.
     * @param event The {@link LoanReturnedEvent loan returned event} to publish.
     * @throws IllegalArgumentException Thrown if the key is null or blank, or if the event is null.
     */
   public void publish(String key, LoanReturnedEvent event) {
        publish(loanReturnTopic, key, event, LoanEventType.LOAN_RETURNED);
    }

    /**
     * Publishes a {@link LoanCreatedEvent loan created event} to the loan created topic.
     * @param key The message key.
     * @param event The {@link LoanCreatedEvent loan created event} to publish.
     * @throws IllegalArgumentException Thrown if the key is null or blank, or if the event is null.
     */
    public void publish(String key, LoanCreatedEvent event) {
        publish(loanCreateTopic, key, event, LoanEventType.LOAN_CREATED);
    }

    /**
     * Publishes a {@link LoanLostEvent loan lost event} to the loan lost topic.
     * @param key The message key.
     * @param event The {@link LoanLostEvent loan lost event} to publish.
     * @throws IllegalArgumentException Thrown if the key is null or blank, or if the event is null.
     */
    public void publish(String key, LoanLostEvent event) {
        publish(loanLostTopic, key, event, LoanEventType.LOAN_MARKED_LOST);
    }

    private void publish(NewTopic topic, String key, Object event, LoanEventType eventType) {
        if(StringUtils.isBlank(key)){
            throw new IllegalArgumentException("The message key cannot be null or blank");
        }
        if(event == null){
            throw new IllegalArgumentException("The event to publish cannot be null");
        }
        var message = MessageBuilder.withPayload(event).
                setHeader(KafkaHeaders.TOPIC, topic.name()).
                setHeader(KafkaHeaders.KEY, key).
                setHeader("eventType", eventType.getValue()).
                build();
        template.send(message);
    }




}
