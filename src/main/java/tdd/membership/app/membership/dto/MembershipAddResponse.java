package tdd.membership.app.membership.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tdd.membership.app.membership.model.Membership;
import tdd.membership.app.membership.model.MembershipType;

@Getter
@RequiredArgsConstructor
public class MembershipAddResponse {

    private final Long id;
    private final MembershipType membershipType;

    public static MembershipAddResponse of(Membership membership) {
        return new MembershipAddResponse(membership.getId(), membership.getMembershipType());
    }
}
