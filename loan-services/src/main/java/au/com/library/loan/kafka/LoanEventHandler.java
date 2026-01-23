package au.com.library.loan.kafka;

import au.com.library.loan.kafka.event.LoanCreatedEvent;
import au.com.library.loan.kafka.event.LoanEvent;
import au.com.library.loan.kafka.event.LoanLostEvent;
import au.com.library.loan.kafka.event.LoanReturnedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Handles loan events and publishes them to Kafka.
 *
 * @see LoanEventPublisher
 */
//@Component
@RequiredArgsConstructor
public class LoanEventHandler {

    private final LoanEventPublisher eventPublisher;

    /**
     * Handles a {@link LoanCreatedEvent loan created event} by publishing it to Kafka.
     *
     * @param event The {@link LoanCreatedEvent loan created event} to handle.
     */
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    public void handle(LoanEvent event){
//        eventPublisher.publish(event);
//    }
}
