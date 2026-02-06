package au.com.library.loan.repository;

import au.com.library.loan.entity.HoldAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing HoldAllocation entities in the database.
 */
public interface HoldAllocationRepository extends JpaRepository<HoldAllocation, Long> {
}
