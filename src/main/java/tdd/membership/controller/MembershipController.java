package tdd.membership.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tdd.membership.dto.MembershipDetailResponse;
import tdd.membership.dto.MembershipRequest;
import tdd.membership.dto.MembershipAddResponse;
import tdd.membership.service.MembershipService;

import javax.validation.Valid;

import java.util.List;

import static tdd.membership.constants.MembershipConstants.USER_ID_HEADER;

@RestController
@RequiredArgsConstructor
public class MembershipController {

    private final MembershipService membershipService;

    @GetMapping("/api/v1/memberships/{id}")
    public ResponseEntity<MembershipDetailResponse> getMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable final Long id) {

        return ResponseEntity.ok(membershipService.getMembership(id, userId));
    }


    @GetMapping("/api/v1/memberships")
    public ResponseEntity<List<MembershipDetailResponse>> getMembershiplist(
            @RequestHeader(USER_ID_HEADER) final String userId) {

        return ResponseEntity.status(HttpStatus.OK).body(membershipService.getMembershipList(userId));
    }

    @PostMapping("/api/v1/memberships")
    public ResponseEntity<MembershipAddResponse> addMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @RequestBody @Valid final MembershipRequest membershipRequest) {

        final MembershipAddResponse membershipAddResponse = membershipService.addMembership(
                userId,
                membershipRequest.getMembershipType(),
                membershipRequest.getPoint()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(membershipAddResponse);
    }

    @DeleteMapping("/api/v1/memberships/{id}")
    public ResponseEntity<Void> removeMembership(
            @RequestHeader(USER_ID_HEADER) final String userId,
            @PathVariable Long id) {

        membershipService.removeMembership(id, userId);

        return ResponseEntity.noContent().build();
    }

}
