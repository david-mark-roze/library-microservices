package au.com.library.loan.repository;

import au.com.library.loan.entity.HoldRequest;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for managing HoldRequest entities. This interface extends JpaRepository, providing CRUD operations and query methods for HoldRequest entities.
 */
public interface HoldRequestRepository extends JpaRepository<HoldRequest, Long> {

     static final String ACTIVE_HOLD_REQUESTS_BY_EDITION_ID_QUERY = """
              select hr from HoldRequest hr
              where hr.editionId = :editionId
                and hr.status = :#{T(au.com.library.loan.entity.HoldRequestStatus).ACTIVE}
              order by hr.requestedAt asc, hr.id asc
            """;

     static final String OPEN_HOLD_REQUESTS_BY_EDITION_ID_QUERY = """
              select hr from HoldRequest hr
              where hr.editionId = :editionId
                and hr.status in (
                  :#{T(java.util.List).of(
                      T(au.com.library.loan.entity.HoldRequestStatus).ACTIVE,
                      T(au.com.library.loan.entity.HoldRequestStatus).ALLOCATED
                  )}
                )
              order by hr.requestedAt asc, hr.id asc
            """;
    /**
     * Retrieves a list of active {@link HoldRequest} entities for a given edition ID, ordered by the {@link HoldRequest#getRequestedAt() requestedAt}  timestamp and hold request id in ascending order.
     * <p>This method applies a pessimistic write lock to prevent concurrent modifications of the retrieved {@link HoldRequest} entities.</p>
     *
     * @param editionId The ID of the edition for which to retrieve active hold requests.
     * @param pageable  The pagination information for the query which may be used to limit the number of results returned.
     * @return A matching list of active {@link HoldRequest} entities. Otherwise, an empty list.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(ACTIVE_HOLD_REQUESTS_BY_EDITION_ID_QUERY)
    List<HoldRequest> lockedActiveHeadHoldRequest(
            @Param("editionId") Long editionId,
            Pageable pageable
    );

    /**
     * Retrieves a list of open {@link HoldRequest} entities for a given edition ID, ordered by the {@link HoldRequest#getRequestedAt() requestedAt}  timestamp and hold request id in ascending order.
     * A hold request is considered 'open' if its status is either {@link au.com.library.loan.entity.HoldRequestStatus#ACTIVE active} or {@link au.com.library.loan.entity.HoldRequestStatus#ALLOCATED allocated}.
     *<p>This method applies a pessimistic write lock to prevent concurrent modifications of the retrieved {@link HoldRequest} entities.</p>
     *
     * @param editionId
     * @param pageable
     * @return
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(OPEN_HOLD_REQUESTS_BY_EDITION_ID_QUERY)
    List<HoldRequest> lockedOpenHoldRequestsByEditionId(
            @Param("editionId") Long editionId,
            Pageable pageable
    );
}
