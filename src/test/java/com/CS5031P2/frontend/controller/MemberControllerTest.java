package com.CS5031P2.frontend.controller;

import com.CS5031P2.backend.model.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Unit tests for the MemberController class.
 */
public class MemberControllerTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MockedStatic<HttpController> mockedHttpController;

    /**
     * Sets up the necessary configurations and mocks before each test method execution.
     */
    @BeforeEach
    public void setUp() {
        // Necessary for code coverage
        MemberController memberController = new MemberController();

        System.setOut(new PrintStream(outputStreamCaptor));
        mockedHttpController = Mockito.mockStatic(HttpController.class);

        // Use doAnswer to simulate the behavior of void methods
        mockedHttpController.when(() -> HttpController.executePostRequest(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending POST request to " + invocation.getArgument(0) + " with body " + invocation.getArgument(1));
                    return null; // Void method, so we return null
                });

        mockedHttpController.when(() -> HttpController.executePutRequest(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending PUT request to " + invocation.getArgument(0) + " with body " + invocation.getArgument(1));
                    return null; // Void method, so we return null
                });

        mockedHttpController.when(() -> HttpController.executeDeleteRequest(anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending DELETE request to " + invocation.getArgument(0));
                    return null; // Void method, so we return null
                });

        // Create a mock JSON response that represents a list of members
        String mockJsonResponse = "[{\"memberId\":\"1\",\"name\":\"John Doe\",\"address\":\"123 Main St\"}," +
                "{\"memberId\":\"2\",\"name\":\"Jane Doe\",\"address\":\"456 Elm St\"}]";

        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending GET request to " + invocation.getArgument(0));
                    return mockJsonResponse; // return the mock json response
                });
    }

    /**
     * Cleans up resources after each test method execution.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        mockedHttpController.close();
    }

    /**
     * Tests the MemberController.addMember() method.
     */
    @Test
    public void testAddMember() {
        String name = "St Andrews";
        String address = "123 North St";
        MemberController.addMember(name, address);
        String expectedOutput = "Sending POST request to http://localhost:8080/members with body {\"address\":\"123 North St\",\"name\":\"St Andrews\"}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the MemberController.removeMember() method.
     */
    @Test
    public void testRemoveMember() {
        String memberId = "1";
        MemberController.removeMember(memberId);
        String expectedOutput = "Sending DELETE request to http://localhost:8080/members/1";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the MemberController.updateMember() method.
     */
    @Test
    public void testUpdateMember() {
        String memberId = "1";
        String newName = "Jane Doe";
        String newAddress = "124 Main St";
        MemberController.updateMember(memberId, newName, newAddress);
        String expectedOutput = "Sending GET request to http://localhost:8080/members\nSending PUT request to http://localhost:8080/members/1 with body {\"address\":\"124 Main St\",\"name\":\"Jane Doe\"}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the MemberController.updateMember() method with no valid memberId.
     */
    @Test
    public void testUpdateMemberWithNoMatchingId() {
        String memberId = "3"; // Assuming 1 and 2 are taken, as per setup
        String newName = "Alice Wonderland";
        String newAddress = "999 Fantasy Rd";
        MemberController.updateMember(memberId, newName, newAddress);

        // Since there's no matching memberId, we don't expect a PUT request
        String expectedOutput = "Sending GET request to http://localhost:8080/members";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput, "Expected only a GET request with no subsequent PUT request");
    }

    /**
     * Tests the MemberController.updateMember() method with a valid list of borrowed books for the member.
     */
    @Test
    public void testUpdateMemberWithBorrowedBooks() {
        String memberId = "1";
        String newName = "John Doe";
        String newAddress = "123 Main St";

        // Mock a member with borrowed books, matching the structure expected by the Book class
        String mockBookJson = "{\"bookId\":\"12345\",\"title\":\"Mock Book\",\"author\":\"Mock Author\",\"borrowedId\":\"" + memberId + "\",\"checkedOut\":true}";
        String mockJsonResponse = "[{\"memberId\":\"1\",\"name\":\"John Doe\",\"address\":\"123 Main St\", \"borrowedBooks\":{\"12345\":" + mockBookJson + "}}]";

        mockedHttpController.when(() -> HttpController.executeGetRequest("http://localhost:8080/members"))
                .thenReturn(mockJsonResponse);

        MemberController.updateMember(memberId, newName, newAddress);
        String expectedOutput = "Sending GET request to http://localhost:8080/members\n" +
                "Sending PUT request to http://localhost:8080/members/1 with body " +
                "{\"address\":\"123 Main St\"," +
                "\"borrowedBooks\":{\"12345\":{\"isCheckedOut\":true,\"borrowedId\":\"1\",\"author\":\"Mock Author\",\"title\":\"Mock Book\",\"bookId\":\"12345\"}},\"name\":\"John Doe\"}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }
    
    /**
     * Tests the MemberController.getAllMembers() method.
     */
    @Test
    public void testGetAllMembers() {
        List<Member> members = MemberController.getAllMembers();

        // Assertions to verify the properties of the returned Member objects
        assertEquals(2, members.size()); // Verify the size of the list
        assertEquals("John Doe", members.get(0).getName()); // Verify the name of the first member
        assertEquals("456 Elm St", members.get(1).getAddress()); // Verify the address of the second member

        // Verify the output if necessary
        String expectedOutput = "Sending GET request to http://localhost:8080/members";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the MemberController.getAllMembers() method when the response is null.
     */
    @Test
    public void testGetAllMembersReturnsEmptyListForNullResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn(null);
        List<Member> members = MemberController.getAllMembers();
        assertTrue(members.isEmpty(), "The list should be empty for a null response");
    }

    /**
     * Tests the MemberController.getAllMembers() method when the response is empty.
     */
    @Test
    public void testGetAllMembersReturnsEmptyListForEmptyResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("");
        List<Member> members = MemberController.getAllMembers();
        assertTrue(members.isEmpty(), "The list should be empty for an empty response");
    }

    /**
     * Tests the MemberController.getAllMembers() method when the response is invalid.
     */
    @Test
    public void testGetAllMembersReturnsEmptyListForInvalidResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("Invalid response");
        List<Member> members = MemberController.getAllMembers();
        assertTrue(members.isEmpty(), "The list should be empty for a response that does not start with '['");
    }
}
