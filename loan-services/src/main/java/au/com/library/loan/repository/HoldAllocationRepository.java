package au.com.library.loan.repository;

import au.com.library.loan.entity.HoldAllocation;
import au.com.library.loan.entity.HoldAllocationStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link HoldAllocation} entities in the database.
 */
public interface HoldAllocationRepository extends JpaRepository<HoldAllocation, Long> {

    static final String EXPIRED_ALLOCATIONS_QUERY = """
              select ha.id from HoldAllocation ha
              where ha.expiryDate < current_date
                and ha.status = :#{T(au.com.library.loan.entity.HoldAllocationStatus).ALLOCATED}
            """;

    static final String FIND_ALLOCATION_QUERY = "select ha from HoldAllocation ha where ha.id = :id";

    /**
     * Returns a list of IDs for all {@link HoldAllocation} entities that have expired. In this context, an allocation is considered expired if its expiry date is before the current date and its status is {@link HoldAllocationStatus#ALLOCATED allocated}.
     * Will be used by a scheduled task to identify expired allocations and update their status to {@link HoldAllocationStatus#EXPIRED expired}.
     * @return A list of IDs for all expired {@link HoldAllocation} entities. Otherwise, an empty list.
     */
    @Query(EXPIRED_ALLOCATIONS_QUERY)
    List<Long> findExpired();

    /**
     * Retrieves an {@link HoldAllocationStatus#ALLOCATED allocated} {@link HoldAllocation} entity by its ID and applies a pessimistic write lock to it.
     * This ensures that the retrieved {@link HoldAllocation} entity is locked for update, preventing concurrent modifications by other transactions until the current transaction is completed.
     * @param id The ID of allocated the {@link HoldAllocation} entity to retrieve and lock for update.
     * @return An {@link Optional} containing the allocated {@link HoldAllocation} entity with the specified ID if it exists, or an empty {@link Optional} if no such entity exists.
     */
    @Query(FIND_ALLOCATION_QUERY)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<HoldAllocation> findAllocatedByIdForUpdate(Long id);
}
