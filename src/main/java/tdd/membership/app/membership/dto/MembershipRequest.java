package tdd.membership.app.membership.dto;

import lombok.*;
import tdd.membership.app.membership.model.MembershipType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MembershipRequest {

    @NotNull
    @Min(0)
    private final Integer point;

    @NotNull
    private final MembershipType membershipType;

    public static MembershipRequest createMembershipRequest(Integer point, MembershipType membershipType) {
        return new MembershipRequest(point, membershipType);
    }
}
