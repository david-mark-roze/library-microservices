package au.com.library.loan.entity;

import au.com.library.loan.util.ValidationUtil;
import au.com.library.shared.exception.ConflictException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a hold request made by a library member for a specific book edition.
 *
 * @see HoldAllocation
 */
// Lombok annotation to generate a no-args constructor with protected access level and force initialization of final fields.
// This will restrict direct instantiation while allowing JPA to create instances via reflection.
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@Entity
public class HoldRequest {

    private static final String HOLD_KEY_FORMAT = "E%s-M%s";

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(nullable = false)
    private final Long memberId;

    @Getter
    @Column(nullable = false)
    private final String memberFirstName;

    @Getter
    @Column(nullable = false)
    private final String memberLastName;

    @Getter
    @Column(nullable = false)
    private final String email;

    @Getter
    @Column(nullable = false)
    private final String phone;

    @Getter
    @Column(nullable = false)
    private final Long editionId;

    @Getter
    @Column(nullable = false)
    private final String bookTitle;

    @Getter
    @Column(nullable = false)
    private final String author;

    @Getter
    @Column(nullable = false)
    private final String edition;

    @Enumerated(EnumType.STRING)
    @Getter
    @Column(nullable = false)
    private HoldRequestStatus status;

    @Getter
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    /**
     *  Unique key for identifying open hold requests for a specific edition and member.
     *  Used to enforce a uniqueness constraint on open holds.
     */
    private String openHoldKey;

    @Getter
    // Bidirectional one-to-one where the child HoldAllocation references this object (its parent) by the name 'holdRequest'.
    @OneToOne(mappedBy = "holdRequest", fetch = FetchType.LAZY)
    private HoldAllocation allocation;

    /**
     * Builder for creating HoldRequest instances.
     */
   public static final class Builder {
        private Long memberId;
        private String memberLastName;
        private String memberFirstName;
        private String email;
        private String phone;
        private Long editionId;
        private String bookTitle;
        private String author;
        private String edition;

        /**
         * Sets the member ID for the hold request. The provided member ID must be non-null and greater than zero, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param memberId the unique identifier of the member making the hold request; must be non-null and greater than zero.
         * @return the Builder instance for method chaining.
         */
        public Builder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        /**
         * Sets the member's last name for the hold request. The provided last name must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param memberLastName the last name of the member making the hold request; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder memberLastName(String memberLastName) {
            this.memberLastName = memberLastName;
            return this;
        }

        /**
         * Sets the member's first name for the hold request. The provided first name must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param memberFirstName the first name of the member making the hold request; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder memberFirstName(String memberFirstName) {
            this.memberFirstName = memberFirstName;
            return this;
        }

        /**
         * Sets the member's email for the hold request. The provided email must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param email the email of the member making the hold request; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        /**
         * Sets the member's phone number for the hold request. The provided phone number must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param phone the phone number of the member making the hold request; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        /**
         * Sets the edition ID for the hold request. The provided edition ID must be non-null and greater than zero, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param editionId the unique identifier of the book edition for which the hold request is being made; must be non-null and greater than zero.
         * @return the Builder instance for method chaining.
         */
        public Builder editionId(Long editionId) {
            this.editionId = editionId;
            return this;
        }

        /**
         * Sets the book title for the hold request. The provided book title must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param bookTitle the title of the book for which the hold request is being made; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        /**
         * Sets the book author for the hold request. The provided author must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param author the author of the book for which the hold request is being made; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder author(String author) {
            this.author = author;
            return this;
        }

        /**
         * Sets the book edition for the hold request. The provided edition must be non-null, or an IllegalArgumentException will be thrown when building the HoldRequest.
         *
         * @param edition the edition of the book for which the hold request is being made; must be non-null.
         * @return the Builder instance for method chaining.
         */
        public Builder edition(String edition) {
            this.edition = edition;
            return this;
        }

        /**
         * Builds a new HoldRequest instance with the provided parameters. All parameters must be valid according to their respective setter methods, or an IllegalArgumentException will be thrown.
         *
         * @return a new HoldRequest instance with the specified parameters.
         * @throws IllegalArgumentException if any of the parameters are invalid according to their respective setter methods.
         */
        public HoldRequest build() {
            return new HoldRequest(memberId, memberLastName, memberFirstName, email, phone, editionId, bookTitle, author, edition);
        }
    }

    /**
     * Creates a new Builder instance for constructing HoldRequest objects.
     *
     * @return a new Builder instance for constructing HoldRequest objects.
     */
    public static Builder builder() {
        return new Builder();
    }

    private HoldRequest(Long memberId, String memberLastName, String memberFirstName, String email, String phone, Long editionId, String bookTitle, String author, String edition) {
        this.memberId = (Long) ValidationUtil.checkNonNullPositiveNumber(memberId, "Member ID must be non-null and greater than zero");
        this.memberLastName = ValidationUtil.checkNonNullParameter(memberLastName, "Member last name must be provided");
        this.memberFirstName = ValidationUtil.checkNonNullParameter(memberFirstName,"Member first name must be provided");
        this.email = ValidationUtil.checkNonNullParameter(email,"Member email must be provided");
        this.phone = ValidationUtil.checkNonNullParameter(phone,"Member phone must be provided");
        this.editionId = (Long) ValidationUtil.checkNonNullPositiveNumber(editionId,"Edition ID must be non-null and greater than zero");
        this.bookTitle = ValidationUtil.checkNonNullParameter(bookTitle,"Book title must be provided");
        this.author = ValidationUtil.checkNonNullParameter(author,"Book author must be provided");
        this.edition = ValidationUtil.checkNonNullParameter(edition, "Book edition must be provided");
        this.requestedAt = LocalDateTime.now();
        this.status = HoldRequestStatus.ACTIVE;
        this.openHoldKey = String.format(HOLD_KEY_FORMAT, editionId, memberId);
    }

    /**
     * Indicates whether this hold request has been allocated to a copy. A hold request is considered allocated if it has an associated {@link HoldAllocation hold allocation}.
     * @return true if the hold request has an associated hold allocation, false otherwise.
     */
    public boolean hasAllocation() {
        return allocation != null;
    }

    /**
     * Marks this hold request as allocated by updating its status to {@link HoldRequestStatus#ALLOCATED allocated}.
     * This method should be called just before creating a corresponding {@link HoldAllocation hold allocation} for this hold request to ensure the status is updated in the same transaction as the allocation creation.
     */
    public void markAsAllocated() {
        if(!status.isActive()){
            throw new ConflictException(String.format("Hold request with ID %d cannot be marked as allocated because its current status is %s.", id, status));
        }
        this.status = HoldRequestStatus.ALLOCATED;
    }

    /**
     * Marks this hold request as completed by updating its status to {@link HoldRequestStatus#COMPLETED completed}.
     * This method should be called just before the member collects the allocated edition copy for loaning to indicate that the hold request has been fulfilled.
     */
    public void markAsCompleted() {
        if(!status.isAllocated()){
            throw new ConflictException(String.format("Hold request with ID %d cannot be marked as completed because its current status is %s.", id, status));
        }
        this.status = HoldRequestStatus.COMPLETED;
        this.openHoldKey = null;
    }

    /**
     * Marks this hold request as cancelled by updating its status to {@link HoldRequestStatus#CANCELLED cancelled}.
     * A hold request can be cancelled if it is currently active or allocated. If the hold request is already completed, it cannot be cancelled and a ConflictException will be thrown.
     */
    public void markAsCancelled() {
        if(status.isCompleted()){
            throw new ConflictException(String.format("Hold request with ID %d cannot be marked as cancelled because it has already been completed.", id));
        }
        this.status = HoldRequestStatus.CANCELLED;
        this.openHoldKey = null;
    }
}
