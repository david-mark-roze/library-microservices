package au.com.library.book.kafka;

import au.com.library.book.kafka.event.LoanEvent;
import au.com.library.book.service.EditionCopyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoanEventSubscriber {

    private static final String EVENT_TYPE = "eventType";

    private final EditionCopyService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topic.loan-event}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleEvent(@Payload LoanEvent eventPayload) {
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
