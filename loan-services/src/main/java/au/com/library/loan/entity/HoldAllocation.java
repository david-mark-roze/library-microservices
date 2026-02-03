package au.com.library.loan.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
    private final HoldRequest holdRequest;

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
    private LocalDate allocationDate;

    @Getter
    private LocalDate expiryDate;

    private Long openCopyId;

    /**
     * Builder for creating HoldAllocation instances.
     */
    public static final class Builder {
        private HoldRequest holdRequest;
        private Long editionCopyId;
        private String barcode;

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

        public HoldAllocation build() {
            return new HoldAllocation(holdRequest, editionCopyId, barcode);
        }
    }

    private HoldAllocation(HoldRequest holdRequest, Long editionCopyId, String barcode) {
        this.holdRequest = holdRequest;
        this.editionCopyId = editionCopyId;
        this.barcode = barcode;
        this.status = HoldAllocationStatus.ALLOCATED;
        this.allocationDate = LocalDate.now();
        this.openCopyId = editionCopyId;
    }


}
