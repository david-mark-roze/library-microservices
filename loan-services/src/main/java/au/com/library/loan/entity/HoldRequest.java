package au.com.library.loan.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * Entity representing a hold request made by a library member for a specific book edition.
 *
 * @see HoldAllocation
 */
@Entity
public class HoldRequest {

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

    private String openHoldKey;

    @Getter
    // Bidirectional one-to-one where the child HoldAllocation references this object (its parent) by the name 'holdRequest'.
    @OneToOne(mappedBy = "holdRequest", fetch = FetchType.LAZY)
    private HoldAllocation allocation;

    public HoldRequest(Long memberId, String memberLastName, String memberFirstName, String email, String phone, Long editionId, String bookTitle, String author, String edition) {
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
    }
}
