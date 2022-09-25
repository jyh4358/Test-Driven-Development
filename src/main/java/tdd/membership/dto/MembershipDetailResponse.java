package tdd.membership.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tdd.membership.model.Membership;
import tdd.membership.model.MembershipType;

@Getter
@RequiredArgsConstructor
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;

    public static MembershipDetailResponse of(Membership membership) {
        return new MembershipDetailResponse(membership.getId(), membership.getMembershipType());
    }
}
