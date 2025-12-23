package au.com.library.loan.client;

import au.com.library.loan.client.config.ClientErrorDecoderConfig;
import au.com.library.loan.dto.MemberSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign Client interface that handles requests to the library members service.
 */
@FeignClient(
        name="member-services",
        url = "${services.members.base-url}",
        configuration = ClientErrorDecoderConfig.class
)
public interface MemberClient {

    @GetMapping("/api/members/{memberId}")
    MemberSnapshotDTO findMember(@PathVariable Long memberId);
}
