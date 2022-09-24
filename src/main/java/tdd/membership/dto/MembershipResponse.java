package tdd.membership.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tdd.membership.model.Membership;
import tdd.membership.model.MembershipType;

@Getter
@RequiredArgsConstructor
public class MembershipResponse {

    private final Long id;
    private final MembershipType membershipType;

    public static MembershipResponse of(Membership membership) {
        return new MembershipResponse(membership.getId(), membership.getMembershipType());
    }
}
