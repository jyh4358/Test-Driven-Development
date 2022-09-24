package tdd.membership.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tdd.membership.model.Membership;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
}
