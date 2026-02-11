package au.com.library.loan.service;

import org.springframework.stereotype.Service;

/**
 * Service interface for managing hold allocations in the library system.
 * A hold allocation represents a temporary reservation of an edition copy for a member
 * who has placed a hold on it. This service provides functionality such as expiring hold allocations that have exceeded their expiration time.
 */
public interface HoldAllocationService {

    /**
     * Expires hold allocations that have exceeded their expiration time.
     * This method should be called periodically (e.g., via a scheduled task)
     * to ensure that expired hold allocations are removed and the associated
     * edition copies are made available for other members.
     */
    void expireAllocations();

    /**
     * Expires a specific hold allocation. This method is intended to be called by the
     * scheduled task that processes expired allocations.
     *
     * @param id The ID of the hold allocation to expire.
     */
    void expireAllocation(Long id);
}
