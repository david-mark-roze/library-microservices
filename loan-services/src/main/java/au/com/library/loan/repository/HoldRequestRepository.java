package au.com.library.loan.repository;

import au.com.library.loan.entity.HoldRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldRequestRepository extends JpaRepository<HoldRequest, Long> {
}
