package tdd.membership.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tdd.membership.dto.MembershipAddResponse;
import tdd.membership.dto.MembershipDetailResponse;
import tdd.membership.exception.MembershipErrorResult;
import tdd.membership.exception.MembershipException;
import tdd.membership.model.Membership;
import tdd.membership.model.MembershipType;
import tdd.membership.repository.MembershipRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipAddResponse addMembership(final String userId, final MembershipType membershipType, final Integer point) {
        final Membership result = membershipRepository.findByUserIdAndMembershipType(userId, membershipType);
        if (result != null) {
            throw new MembershipException(MembershipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        final Membership membership = Membership.builder()
                .userId(userId)
                .membershipType(membershipType)
                .point(point)
                .build();

        Membership savedMembership = membershipRepository.save(membership);

        return MembershipAddResponse.of(savedMembership);
    }

    public List<MembershipDetailResponse> getMembershipList(final String userId) {
        return membershipRepository.findAllByUserId(userId).stream()
                .map(MembershipDetailResponse::of)
                .collect(Collectors.toList());
    }

    public MembershipDetailResponse getMembership(final Long membershipId, final String userId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new MembershipException(MembershipErrorResult.MEMBERSHIP_NOT_FOUND));
        if (!membership.getUserId().equals(userId)) {
            throw new MembershipException(MembershipErrorResult.NOT_MEMBERSHIP_OWNER);
        }

        return MembershipDetailResponse.of(membership);
    }
}
