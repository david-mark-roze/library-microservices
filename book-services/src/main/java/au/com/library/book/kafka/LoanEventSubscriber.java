package au.com.library.book.kafka;

import au.com.library.contracts.event.loan.LoanEvent;
import au.com.library.book.service.EditionCopyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Subscribes to loan events from Kafka and updates edition copy statuses accordingly.
 */
@Component
@RequiredArgsConstructor
public class LoanEventSubscriber {

    private final EditionCopyService service;
    private final ObjectMapper objectMapper;

    /**
     * Subscribes to loan events and processes them based on their type.
     *
     * @param eventPayload The payload of the loan event.
     * @throws IllegalArgumentException if the event type is unknown or the payload is null.
     */
    @KafkaListener(
            topics = "${spring.kafka.topic.loan-event}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void subscribe(@Payload LoanEvent eventPayload) {
        if(eventPayload == null){
            throw new IllegalArgumentException("The event payload cannot be null");
        }
         switch (eventPayload.eventType()) {
            case LOAN_CREATED -> service.borrowCopy(eventPayload.context().editionCopyId());
            case LOAN_RETURNED -> service.returnCopy(eventPayload.context().editionCopyId());
            case LOAN_MARKED_LOST -> service.markCopyLost(eventPayload.context().editionCopyId());
            default -> throw new IllegalArgumentException("Unknown event type: " + eventPayload.eventType());
        }
    }
}
