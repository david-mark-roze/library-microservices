package au.com.library.loan.kafka.event;

public record LoanEvent(LoanEventType eventType, LoanEventContext context) {
}
