package tdd.membership.app.membership.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import tdd.membership.app.membership.model.Membership;
import tdd.membership.app.membership.model.MembershipType;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MembershipDetailResponse {
    private final Long id;
    private final MembershipType membershipType;
    private final Integer point;
    private final LocalDateTime createdAt;

    public static MembershipDetailResponse of(Membership membership) {
        return new MembershipDetailResponse(
                membership.getId(),
                membership.getMembershipType(),
                membership.getPoint(),
                membership.getCreatedAt()
        );
    }
}
