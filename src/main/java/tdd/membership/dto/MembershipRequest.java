package tdd.membership.dto;

import lombok.*;
import tdd.membership.model.MembershipType;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {
    private final Integer point;
    private final MembershipType membershipType;

    public static MembershipRequest createMembershipRequest(Integer point, MembershipType membershipType) {
        return new MembershipRequest(point, membershipType);
    }
}
