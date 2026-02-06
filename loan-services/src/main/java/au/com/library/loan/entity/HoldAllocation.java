package au.com.library.loan.entity;

import au.com.library.shared.util.Numbers;
import jakarta.persistence.*;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing the allocation of a specific edition copy to fulfill a hold request.
 *
 * @see HoldRequest
 */
@Entity
public class HoldAllocation {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    // Bidirectional one-to-one where this HoldAllocation references its parent HoldRequest by the name 'holdRequest'.
    @JoinColumn(name = "hold_request_id", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private HoldRequest holdRequest;

    @Getter
    @Column(nullable = false)
    private final Long editionCopyId;

    @Getter
    @Column(nullable = false)
    private final String barcode;

    @Enumerated(EnumType.STRING)
    @Getter
    @Column(nullable = false)
    private HoldAllocationStatus status;

    @Getter
    @Column(nullable = false)
    private LocalDateTime allocatedAt;

    @Getter
    private LocalDate expiryDate;

    private Long openCopyId;

    private Duration allocationDuration;

    /**
     * Builder for creating HoldAllocation instances.
     */
    public static final class Builder {

        // Using system default clock for simplicity; can be injected for testing purposes.
        private Clock clock = Clock.systemDefaultZone();

        private HoldRequest holdRequest;
        private Long editionCopyId;
        private String barcode;
        private LocalDateTime allocatedAt;
        private Duration allocationDuration;

        Builder() {
            allocatedAt = LocalDateTime.now(clock);
        }

        /**
         * Allows injection of a custom clock for testing purposes. If a null clock is provided, an IllegalArgumentException will be thrown.
         * @param clock the Clock to use for determining the allocatedAt time; must not be null.
         * @return the Builder instance for method chaining.
         */
        public Builder clock(Clock clock) {
            this.clock = Objects.requireNonNullElseGet(clock, () -> {
                throw new IllegalArgumentException("Clock cannot be null");
            });
            return this;
        }

        public Builder holdRequest(HoldRequest holdRequest) {
            this.holdRequest = holdRequest;
            return this;
        }

        public Builder editionCopyId(Long editionCopyId) {
            this.editionCopyId = editionCopyId;
            return this;
        }

        public Builder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public Builder allocationDuration(Duration allocationDuration){
            this.allocationDuration = allocationDuration;
            return this;
        }

        public HoldAllocation build() {
            return new HoldAllocation(holdRequest, editionCopyId, barcode, allocatedAt, allocationDuration);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Marks this hold allocation as collected, which should only be done when the associated hold request is in the allocated state.
     * @throws IllegalStateException if the hold request is not in the allocated state.
     */
    public void markAsCollected() {
        if (!status.isAllocated()) {
            throw new IllegalStateException("Hold allocation must be allocated to be marked as collected.");
        }
        this.status = HoldAllocationStatus.COLLECTED;
    }

    private HoldAllocation(HoldRequest holdRequest, Long editionCopyId, String barcode, LocalDateTime allocatedAt, Duration allocationDuration) {
        this.holdRequest = checkNonNullParameter(holdRequest, "HoldRequest cannot be null");
        this.editionCopyId = (Long)checkValidNumber(editionCopyId, "Edition copy id must be non-null and greater than zero");
        this.barcode = checkNonNullParameter(barcode, "Barcode cannot be null");
        this.status = HoldAllocationStatus.ALLOCATED;
        this.allocatedAt = checkNonNullParameter(allocatedAt, "Allocation time cannot be null");
        this.allocationDuration = checkNonNullParameter(allocationDuration, "Allocation duration cannot be null");
        this.openCopyId = editionCopyId;
        this.expiryDate = allocatedAt.plus(allocationDuration).toLocalDate();
    }

    private <T> T checkNonNullParameter(T object, String message){
        return Objects.requireNonNullElseGet(object, () -> {;
            throw new IllegalArgumentException(message);
        });
    }

    private Number checkValidNumber(Number number, String message){
        return Numbers.isNonNullPositiveOrElseThrow(editionCopyId, ()-> new IllegalArgumentException(message));
    }

}
