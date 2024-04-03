package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.LibraryLendingSystem;
import com.CS5031P2.backend.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This class contains test cases for the MemberController class.
 */
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MemberController memberController;

    @MockBean
    private LibraryLendingSystem libraryLendingSystem;

    /**
     * Test case to verify that MemberController.addMember(Member) successfully adds a member.
     */
    @Test
    void addMemberSuccessfully() {
        // Arrange
        Member newMember = new Member("John Doe", "123 Main St");

        doNothing().when(libraryLendingSystem).addMember(newMember.getName(), newMember.getAddress());

        // Act
        ResponseEntity<String> responseEntity = memberController.addMember(newMember);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Member created successfully", responseEntity.getBody());
    }

    /**
     * Test case to verify that MemberController.deleteMember(String) successfully deletes a member.
     */
    @Test
    void deleteMemberSuccessfully() {
        // Arrange
        String memberId = "1";

        doNothing().when(libraryLendingSystem).deleteMember(memberId);

        // Act
        ResponseEntity<String> responseEntity = memberController.deleteMember(memberId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Member deleted successfully", responseEntity.getBody());
    }

    /**
     * Test case to verify that MemberController.updateMember(String, Member) updates a member successfully.
     */
    @Test
    void updateMemberSuccessfully() {
        // Arrange
        String memberId = "2";
        Member updatedMember = new Member("Jane Doe", "456 Main St");

        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.of(new Member("John Doe", "123 Main St")));
        doNothing().when(libraryLendingSystem).updateMember(eq(memberId), any(Member.class));

        // Act
        ResponseEntity<String> responseEntity = memberController.updateMember(memberId, updatedMember);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Member updated successfully", responseEntity.getBody());
    }

    /**
     * Test case to verify that MemberController.updateMember(String, Member) returns HttpStatus.NOT_FOUND when the member does not exist.
     */
    @Test
    void updateMemberNotFound() {
        // Arrange
        String nonExistingMemberId = "3";
        Member updatedMember = new Member("Jane Doe", "456 Main St");

        when(libraryLendingSystem.getMember(nonExistingMemberId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> responseEntity = memberController.updateMember(nonExistingMemberId, updatedMember);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Test case to verify that MemberController.getAllUsersWithAdminInfo() returns a list of members.
     */
    @Test
    void getAllMembersReturnsMembersList() {
        // Arrange
        List<Member> members = new ArrayList<>();
        members.add(new Member("John Doe", "123 Main St"));
        members.add(new Member("Jane Doe", "456 Main St"));

        members.get(0).setMemberId("1");
        members.get(1).setMemberId("2");

        when(libraryLendingSystem.getMembers()).thenReturn(createMemberMap(members));

        // Act
        ResponseEntity<List<Member>> responseEntity = memberController.getAllUsersWithAdminInfo();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(members, responseEntity.getBody());
    }

    /**
     * Test case to verify that MemberController.getAllUsersWithAdminInfo() returns HttpStatus.NOT_FOUND when the members list is empty.
     */
    @Test
    void getAllMembersReturnsNotFoundForEmptyMemberList() {
        // Arrange
        when(libraryLendingSystem.getMembers()).thenReturn(Collections.emptyMap());

        // Act
        ResponseEntity<List<Member>> responseEntity = memberController.getAllUsersWithAdminInfo();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Helper method to create a map of members.
     *
     * @param members The list of members.
     * @return A map of members.
     */
    private static Map<String, Member> createMemberMap(List<Member> members) {
        Map<String, Member> memberMap = new HashMap<>();
        for (Member member : members) {
            // Assuming Member class has a getId method to retrieve a unique identifier
            memberMap.put(member.getMemberId(), member);
        }
        return memberMap;
    }
}
