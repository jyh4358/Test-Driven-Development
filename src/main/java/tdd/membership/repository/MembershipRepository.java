package tdd.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdd.membership.model.Membership;
import tdd.membership.model.MembershipType;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    Membership findByUserIdAndMembershipType(final String userId, final MembershipType membershipType);
}
