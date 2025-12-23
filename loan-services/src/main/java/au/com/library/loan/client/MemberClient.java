package au.com.library.loan.client;

import au.com.library.loan.client.config.ClientConfig;
import au.com.library.loan.dto.MemberSnapshotDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name="member-services",
        url = "${services.members.base-url}",
        configuration = ClientConfig.class
)
public interface MemberClient {

    @GetMapping("/api/members/{memberId}")
    MemberSnapshotDTO findMember(@PathVariable Long memberId);
}
