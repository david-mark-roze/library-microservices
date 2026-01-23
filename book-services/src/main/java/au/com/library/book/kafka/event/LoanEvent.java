package au.com.library.book.kafka.event;

public record LoanEvent(LoanEventType eventType, LoanEventContext context) {
}
