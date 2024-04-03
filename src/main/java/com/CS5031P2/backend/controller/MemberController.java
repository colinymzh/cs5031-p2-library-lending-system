package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.LibraryLendingSystem;
import com.CS5031P2.backend.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing member-related operations in the library system.
 */
@RestController
public class MemberController {

    @Autowired
    LibraryLendingSystem libraryLendingSystem;

    /**
     * Adds a new member to the library system.
     *
     * @param member The Member object representing the new member.
     * @return ResponseEntity indicating the status of the operation.
     */
    @PostMapping("/members")
    public ResponseEntity<String> addMember(@RequestBody Member member) {
        libraryLendingSystem.addMember(member.getName(), member.getAddress());
        return ResponseEntity.status(HttpStatus.CREATED).body("Member created successfully");
    }

    /**
     * Deletes a member from the library system based on the member ID.
     *
     * @param memberId The unique identifier of the member to be deleted.
     * @return ResponseEntity indicating the status of the operation.
     */
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable String memberId) {
        libraryLendingSystem.deleteMember(memberId);
        return ResponseEntity.ok("Member deleted successfully");
    }

    /**
     * Updates the details of an existing member in the library system.
     *
     * @param memberId      The unique identifier of the member to be updated.
     * @param updatedMember The Member object containing the updated information.
     * @return ResponseEntity indicating the status of the operation.
     */
    @PutMapping("/members/{memberId}")
    public ResponseEntity<String> updateMember(@PathVariable String memberId, @RequestBody Member updatedMember) {
        if (libraryLendingSystem.getMember(memberId).isPresent()) {
            libraryLendingSystem.updateMember(memberId, updatedMember);
            return ResponseEntity.ok("Member updated successfully");
        }

        return ResponseEntity.notFound().build(); // Member not found
    }

    /**
     * Retrieves a list of all members in the library system.
     *
     * @return ResponseEntity containing a list of members if available, otherwise returns 404 Not Found.
     */
    @GetMapping("/members")
    public ResponseEntity<List<Member>> getAllUsersWithAdminInfo() {
        List<Member> members = new ArrayList<>(libraryLendingSystem.getMembers().values());
        // Returns ok status if members available, or 404 not found
        return !members.isEmpty() ? ResponseEntity.ok(members) : ResponseEntity.notFound().build();
    }
}
