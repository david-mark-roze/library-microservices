package au.com.library.loan.entity;

import au.com.library.loan.util.ValidationUtil;
import au.com.library.shared.exception.ConflictException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
// Lombok annotation to generate a no-args constructor with protected access level and force initialization of final fields.
// This will restrict direct instantiation while allowing JPA to create instances via reflection.
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
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

        /**
         * Sets the HoldRequest for this allocation. The provided HoldRequest must not be null, or an IllegalArgumentException will be thrown.
         * @param holdRequest the HoldRequest to associate with this allocation; must not be null.
         * @return
         */
        public Builder holdRequest(HoldRequest holdRequest) {
            this.holdRequest = holdRequest;
            return this;
        }

        /**
         * Sets the edition copy ID for this allocation. The provided ID must be a non-null positive number, or an IllegalArgumentException will be thrown.
         * @param editionCopyId the ID of the edition copy being allocated; must be a non-null positive number.
         * @return the Builder instance for method chaining.
         */
        public Builder editionCopyId(Long editionCopyId) {
            this.editionCopyId = editionCopyId;
            return this;
        }

        /**
         * Sets the barcode for this allocation. The provided barcode must not be null or empty, or an IllegalArgumentException will be thrown.
         * @param barcode the barcode of the edition copy being allocated; must not be null or empty.
         * @return the Builder instance for method chaining.
         */
        public Builder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        /**
         * Sets the duration for which this allocation is valid, after which it will expire.
         * The expiry date will be calculated as the allocatedAt time plus the provided duration.
         * The provided duration must not be null, or an IllegalArgumentException will be thrown.
         * @param allocationDuration the duration for which this allocation is valid; must not be null.
         * @return the Builder instance for method chaining.
         */
        public Builder allocationDuration(Duration allocationDuration){
            this.allocationDuration = allocationDuration;
            return this;
        }

        /**
         * Builds a new HoldAllocation instance with the provided parameters. All parameters must be valid according to their respective setter methods, or an IllegalArgumentException will be thrown.
         * @return a new HoldAllocation instance with the specified parameters.
         * @throws IllegalArgumentException if any of the parameters are invalid according to their respective setter methods.
         */
        public HoldAllocation build() {
            return new HoldAllocation(holdRequest, editionCopyId, barcode, allocatedAt, allocationDuration);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Marks this hold allocation as collected, indicating that the member has collected the allocated edition copy. This method will set the openCopyId to null and update the status to COLLECTED.
     * A hold allocation can only be marked as collected if the associated hold request is in the completed state and if this hold allocation is currently in the allocated state. If either of these conditions is not met, an IllegalStateException will be thrown to indicate that the hold allocation cannot be marked as collected.
     *
     * @throws ConflictException Thrown if the associated hold request is not in the completed state or if this hold allocation is not in the allocated state, indicating that it cannot be marked as collected.
     */
    public void markAsCollected() {
        if(!holdRequest.getStatus().isCompleted()){
            throw new ConflictException("Hold request must be completed to mark allocation as collected.");
        }
        if (!status.isAllocated()) {
            throw new ConflictException("Hold allocation must be allocated to be marked as collected.");
        }
        openCopyId = null;
        this.status = HoldAllocationStatus.COLLECTED;
    }

    /**
     * Marks this hold allocation as cancelled, indicating that the allocation has been cancelled and the edition copy is no longer reserved for the hold request. This method will set the openCopyId to null and update the status to CANCELLED.
     * A hold allocation can only be marked as cancelled if the associated hold request is not in the completed state and if this hold allocation is not in the collected state. If either of these conditions is not met, an IllegalStateException will be thrown to indicate that the hold allocation cannot be marked as cancelled.
     *
     * @throws ConflictException If the associated hold request is in the completed state or if this hold allocation is in the collected state, indicating that it cannot be marked as cancelled.
     */
    public void markAsCancelled() {
        if (holdRequest.getStatus().isCompleted()) {
            throw new ConflictException("Hold request is already completed and therfore the allocation cannot be cancelled.");
        }
        if (status.isCollected()) {
            throw new ConflictException("Hold allocation is already collected and therfore cannot be cancelled.");
        }
        openCopyId = null;
        this.status = HoldAllocationStatus.CANCELLED;
    }

    private HoldAllocation(HoldRequest holdRequest, Long editionCopyId, String barcode, LocalDateTime allocatedAt, Duration allocationDuration) {
        this.holdRequest = ValidationUtil.checkNonNullParameter(holdRequest, "HoldRequest cannot be null");
        this.editionCopyId = (Long)ValidationUtil.checkNonNullPositiveNumber(editionCopyId, "Edition copy id must be non-null and greater than zero");
        this.barcode = ValidationUtil.checkNonNullParameter(barcode, "Barcode cannot be null");
        this.status = HoldAllocationStatus.ALLOCATED;
        this.allocatedAt = ValidationUtil.checkNonNullParameter(allocatedAt, "Allocation time cannot be null");
        this.openCopyId = editionCopyId;
        this.expiryDate = allocatedAt.plus(
                        ValidationUtil.checkNonNullParameter(
                        allocationDuration,
                        "Allocation duration cannot be null"))
                .toLocalDate();
    }

}
