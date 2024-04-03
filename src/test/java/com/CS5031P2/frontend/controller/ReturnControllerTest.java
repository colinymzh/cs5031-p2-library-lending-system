package com.CS5031P2.frontend.controller;

import com.CS5031P2.backend.model.Book;
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
 * Unit tests for the ReturnController class.
 */
public class ReturnControllerTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MockedStatic<HttpController> mockedHttpController;

    /**
     * Sets up the necessary configurations and mocks before each test method execution.
     */
    @BeforeEach
    public void setUp() {
        // Necessary for code coverage
        ReturnController returnController = new ReturnController();

        System.setOut(new PrintStream(outputStreamCaptor));
        mockedHttpController = Mockito.mockStatic(HttpController.class);

        mockedHttpController.when(() -> HttpController.executeDeleteRequest(anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending DELETE request to " + invocation.getArgument(0));
                    return null;
                });

        // Mock response for GET requests
        String mockJsonResponse = "[{\"memberId\":\"1\",\"bookId\":\"1\",\"borrowedId\":\"1\",\"author\":\"Author One\",\"title\":\"Title One\",\"checkedOut\":\"True\"}," +
                "{\"memberId\":\"1\",\"bookId\":\"2\",\"borrowedId\":\"1\",\"author\":\"Author Two\",\"title\":\"Title Two\",\"checkedOut\":\"True\"}]";

        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending GET request to " + invocation.getArgument(0));
                    return mockJsonResponse;
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
     * Tests the ReturnController.getBorrowedBooksByMember() method.
     */
    @Test
    public void testGetAllBorrowedBooks() {
        String memberId = "1";
        List<Book> borrowedBooks = ReturnController.getBorrowedBooksByMember(memberId);
        assertEquals(2, borrowedBooks.size());
        assertEquals("1", borrowedBooks.get(0).getBookId());
        assertEquals("Author One", borrowedBooks.get(0).getAuthor());
        assertEquals("2", borrowedBooks.get(1).getBookId());
        assertEquals("Author Two", borrowedBooks.get(1).getAuthor());

        String expectedOutput = "Sending GET request to http://localhost:8080/borrowed/1";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the ReturnController.returnBook() method.
     */
    @Test
    public void testReturnBook() {
        String memberId = "1";
        String bookId = "1";
        ReturnController.returnBook(memberId, bookId);
        String expectedOutput = "Sending DELETE request to http://localhost:8080/checkout/1/1";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the ReturnController.getBorrowedBooksByMember() method when the response is null.
     */
    @Test
    public void testGetBorrowedBooksByMemberReturnsEmptyListForNullResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn(null);
        List<Book> borrowedBooks = ReturnController.getBorrowedBooksByMember("memberId");
        assertTrue(borrowedBooks.isEmpty(), "The list should be empty for a null response");
    }

    /**
     * Tests the ReturnController.getBorrowedBooksByMember() method when the response is empty.
     */
    @Test
    public void testGetBorrowedBooksByMemberReturnsEmptyListForEmptyResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("");
        List<Book> borrowedBooks = ReturnController.getBorrowedBooksByMember("memberId");
        assertTrue(borrowedBooks.isEmpty(), "The list should be empty for an empty response");
    }

    /**
     * Tests the ReturnController.getBorrowedBooksByMember() method when the response is invalid.
     */
    @Test
    public void testGetBorrowedBooksByMemberReturnsEmptyListForInvalidResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("Invalid response");
        List<Book> borrowedBooks = ReturnController.getBorrowedBooksByMember("memberId");
        assertTrue(borrowedBooks.isEmpty(), "The list should be empty for a response that does not start with '['");
    }
}
