package au.com.library.loan.kafka;

import au.com.library.loan.kafka.event.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Publishes loan events to Kafka topics.
 */
@Component
@RequiredArgsConstructor
public class LoanEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoanEventPublisher.class);

    private final KafkaTemplate<String, Object> template;
    private final NewTopic loanEventTopic;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(LoanEvent event){
        if(event == null){
            throw new IllegalArgumentException("The event to publish cannot be null");
        }
        var message = MessageBuilder.withPayload(event).
                setHeader(KafkaHeaders.TOPIC, loanEventTopic.name()).
                setHeader(KafkaHeaders.KEY, String.valueOf(event.context().loanId())).
                build();
        template.send(message).whenComplete((_, ex) -> {;
            if (ex != null) {
                LOGGER.error("Failed to send message: " + ex.getMessage());
            }
        });
    }
}
