package au.com.library.loan.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate dateRequested;

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

        public Builder memberId(Long memberId) {
            this.memberId = memberId;
            return this;
        }

        public Builder memberLastName(String memberLastName) {
            this.memberLastName = memberLastName;
            return this;
        }

        public Builder memberFirstName(String memberFirstName) {
            this.memberFirstName = memberFirstName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder editionId(Long editionId) {
            this.editionId = editionId;
            return this;
        }

        public Builder bookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder edition(String edition) {
            this.edition = edition;
            return this;
        }

        public HoldRequest build() {
            return new HoldRequest(memberId, memberLastName, memberFirstName, email, phone, editionId, bookTitle, author, edition);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private HoldRequest(Long memberId, String memberLastName, String memberFirstName, String email, String phone, Long editionId, String bookTitle, String author, String edition) {
        this.memberId = memberId;
        this.memberLastName = memberLastName;
        this.memberFirstName = memberFirstName;
        this.email = email;
        this.phone = phone;
        this.editionId = editionId;
        this.bookTitle = bookTitle;
        this.author = author;
        this.edition = edition;
        this.dateRequested = LocalDate.now();
        this.status = HoldRequestStatus.ACTIVE;
        this.openHoldKey = String.format(HOLD_KEY_FORMAT, editionId, memberId);
    }
}
