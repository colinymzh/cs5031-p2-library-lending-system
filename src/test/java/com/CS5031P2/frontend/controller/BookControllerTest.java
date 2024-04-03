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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Unit tests for the BookController class.
 */
public class BookControllerTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MockedStatic<HttpController> mockedHttpController;

    /**
     * Sets up the necessary configurations and mocks before each test method execution.
     */
    @BeforeEach
    public void setUp() {
        // Necessary for 100% coverage
        BookController bookController = new BookController();

        System.setOut(new PrintStream(outputStreamCaptor));
        mockedHttpController = Mockito.mockStatic(HttpController.class);

        // Mock HTTP controller's methods
        mockedHttpController.when(() -> HttpController.executePostRequest(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending POST request to " + invocation.getArgument(0) + " with body " + invocation.getArgument(1));
                    return null;
                });

        mockedHttpController.when(() -> HttpController.executePutRequest(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending PUT request to " + invocation.getArgument(0) + " with body " + invocation.getArgument(1));
                    return null;
                });

        mockedHttpController.when(() -> HttpController.executeDeleteRequest(anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending DELETE request to " + invocation.getArgument(0));
                    return null;
                });

        // Mock response for GET requests
        String mockJsonResponse = "[{\"bookId\":\"1\", \"borrowedId\":\"\",\"title\":\"Book Title One\",\"author\":\"Author One\",\"checkedOut\":false}," +
                "{\"bookId\":\"2\",\"borrowedId\":\"12345\",\"title\":\"Book Title Two\",\"author\":\"Author Two\",\"checkedOut\":true}]";

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
     * Tests the BookController.addBook() method.
     */
    @Test
    public void testAddBook() {
        String title = "The Great Gatsby";
        String author = "F. Scott Fitzgerald";
        BookController.addBook(title, author);
        String expectedOutput = "Sending POST request to http://localhost:8080/books with body {\"author\":\"F. Scott Fitzgerald\",\"title\":\"The Great Gatsby\"}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the BookController.removeBook() method.
     */
    @Test
    public void testRemoveBook() {
        String bookId = "1";
        BookController.removeBook(bookId);
        String expectedOutput = "Sending DELETE request to http://localhost:8080/books/1";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the BookController.updateBook() method.
     */
    @Test
    public void testUpdateBook() {
        String bookId = "1";
        String newTitle = "New Book Title";
        String newAuthor = "New Author";
        BookController.updateBook(bookId, newTitle, newAuthor);
        String expectedOutput = "Sending PUT request to http://localhost:8080/books/1 with body {\"author\":\"New Author\",\"title\":\"New Book Title\"}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the BookController.getAllBooks() method.
     */
    @Test
    public void testGetAllBooks() {
        List<Book> books = BookController.getAllBooks();
        assertEquals(2, books.size()); // Verify the size of the list
        assertEquals("Book Title One", books.get(0).getTitle()); // Verify the title of the first book
        assertEquals("Author One", books.get(0).getAuthor()); // Verify the author of the first book
        assertFalse(books.get(0).isCheckedOut()); // Verify the check out status of the first book
        assertEquals("Book Title Two", books.get(1).getTitle()); // Verify the title of the second book
        assertEquals("Author Two", books.get(1).getAuthor()); // Verify the author of the second book
        assertTrue(books.get(1).isCheckedOut()); // Verify the check out status of the second book

        String expectedOutput = "Sending GET request to http://localhost:8080/books";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Tests the BookController.getAllBooks() method when the response is null.
     */
    @Test
    public void testGetAllBooksReturnsEmptyListForNullResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn(null);
        List<Book> books = BookController.getAllBooks();
        assertTrue(books.isEmpty(), "The list should be empty for a null response");
    }

    /**
     * Tests the BookController.getAllBooks() method when the response is empty.
     */
    @Test
    public void testGetAllBooksReturnsEmptyListForEmptyResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("");
        List<Book> books = BookController.getAllBooks();
        assertTrue(books.isEmpty(), "The list should be empty for an empty response");
    }

    /**
     * Tests the BookController.getAllBooks() method when the response is invalid.
     */
    @Test
    public void testGetAllBooksReturnsEmptyListForInvalidResponse() {
        mockedHttpController.when(() -> HttpController.executeGetRequest(anyString())).thenReturn("Invalid response");
        List<Book> books = BookController.getAllBooks();
        assertTrue(books.isEmpty(), "The list should be empty for a response that does not start with '['");
    }
}
