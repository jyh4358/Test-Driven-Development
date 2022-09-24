package tdd.membership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdd.membership.exception.MembershipErrorResult;
import tdd.membership.exception.MembershipException;
import tdd.membership.model.Membership;
import tdd.membership.model.MembershipType;
import tdd.membership.repository.MembershipRepository;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public Membership addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);

        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        return null;
    }
}
