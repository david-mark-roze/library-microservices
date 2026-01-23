package au.com.library.book.kafka;

import au.com.library.book.kafka.event.LoanEventContext;
import au.com.library.book.kafka.event.LoanEventEnvelope;
import au.com.library.book.kafka.event.LoanEventType;
import au.com.library.book.service.EditionCopyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.IOException;

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
    public void handleEvent(
            @Header(value = EVENT_TYPE, required = false) String eventType,
            @Payload String eventPayload
    ) {
        System.out.println(">>> LoanEventSubscriber fired: eventType=" + eventType + " payload=" + eventPayload);
        if (StringUtils.isBlank(eventType)) {
            throw new IllegalArgumentException("The event type cannot be null or blank");
        }
        if(StringUtils.isBlank(eventPayload)){
            throw new IllegalArgumentException("The event payload cannot be null or blank");
        }
        LoanEventContext context = extractLoanContext(eventPayload);
        LoanEventType loanEventType = LoanEventType.forValue(eventType);
        switch (loanEventType) {
            case LOAN_CREATED -> service.borrowCopy(context.editionCopyId());
            case LOAN_RETURNED -> service.returnCopy(context.editionCopyId());
            case LOAN_MARKED_LOST -> service.markCopyLost(context.editionCopyId());
            default -> throw new IllegalArgumentException("Unknown event type: " + eventType);
        }
    }

    private LoanEventContext extractLoanContext(String eventPayload) {
        try {
            return objectMapper.readValue(eventPayload, LoanEventEnvelope.class).context();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to parse loan event payload", e);
        }
    }
}
