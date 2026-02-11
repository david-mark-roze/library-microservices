package au.com.library.loan.service.impl;

import au.com.library.loan.entity.HoldAllocation;
import au.com.library.loan.entity.HoldRequest;
import au.com.library.loan.repository.HoldAllocationRepository;
import au.com.library.loan.repository.HoldRequestRepository;
import au.com.library.loan.service.HoldAllocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link HoldAllocationService} interface.
 * This service is responsible for managing hold allocations, including expiring allocations that have exceeded their expiration time.
 */
@Service
@RequiredArgsConstructor
public class HoldAllocationServiceImpl implements HoldAllocationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HoldAllocationServiceImpl.class);

    private final HoldAllocationRepository holdAllocationRepository;
    private final HoldRequestRepository holdRequestRepository;

    // The fixed delay for the scheduled task to expire hold allocations is configured
    // in the application.properties file using the key "loan.allocation-expiry-fixed-delay-ms".
    @Scheduled(fixedDelayString = "${loan.allocation-expiry-fixed-delay-ms}")
    @Override
    public void expireAllocations() {
        LOGGER.info("Starting scheduled task to expire hold allocations...");
        List<Long> allocationIds = holdAllocationRepository.findExpired();
        LOGGER.info("Found {0} expired hold allocations to process...", allocationIds.size());
        if (!allocationIds.isEmpty()) {
            allocationIds.forEach(id -> expireAllocation(id));
        }
        LOGGER.info("Finished scheduled hold allocations expiry task.");
    }

    @Transactional
    public void expireAllocation(Long id) {
        holdAllocationRepository.findAllocatedByIdForUpdate(id)
                .ifPresentOrElse(
                        allocation -> handleAllocationExpiry(allocation),
                        () -> LOGGER.info("The hold allocation with id {0} is no longer allocated. Skipping expiry processing for this allocation.", id)
                );
    }

    private void handleAllocationExpiry(HoldAllocation allocation){
        allocation.expire();
        HoldRequest holdRequest = allocation.getHoldRequest();
        holdAllocationRepository.save(allocation);
        holdRequestRepository.save(holdRequest);
    }

}
