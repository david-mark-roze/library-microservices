package au.com.library.loan.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberSnapshotDTO {

    private Long id;
    private String firstName;
    private String lastName;
}
