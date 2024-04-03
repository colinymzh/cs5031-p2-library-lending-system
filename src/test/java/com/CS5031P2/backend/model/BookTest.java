package com.CS5031P2.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Book class.
 */
class BookTest {
    private Book book;

    /**
     * Sets up the book instance before each test.
     */
    @BeforeEach
    void setUp() {
        book = new Book("War and Peace", "Leo Tolstoy");
        book.setBookId("002");
    }

    /**
     * Tests the retrieval of the book ID.
     */
    @Test
    void getBookId() {
        assertEquals("002", book.getBookId());
    }

    /**
     * Tests the setting of the book ID.
     */
    @Test
    void setBookId() {
        book.setBookId("003");
        assertEquals("003", book.getBookId());
    }

    /**
     * Tests the retrieval of the book title.
     */
    @Test
    void getTitle() {
        assertEquals("War and Peace", book.getTitle());
    }

    /**
     * Tests the setting of the book title.
     */
    @Test
    void setTitle() {
        book.setTitle("Animal Farm");
        assertEquals("Animal Farm", book.getTitle());
    }

    /**
     * Tests the retrieval of the book author.
     */
    @Test
    void getAuthor() {
        assertEquals("Leo Tolstoy", book.getAuthor());
    }

    /**
     * Tests the setting of the book author.
     */
    @Test
    void setAuthor() {
        book.setAuthor("George Orwell");
        assertEquals("George Orwell", book.getAuthor());
    }

    /**
     * Tests the initial checked out status of the book.
     */
    @Test
    void isCheckedOut() {
        assertFalse(book.isCheckedOut());
    }

    /**
     * Tests the setting of the checked out status of the book.
     */
    @Test
    void setIsCheckedOut() {
        book.setCheckedOut(true);
        assertTrue(book.isCheckedOut());
    }

    /**
     * Tests the conversion of the book object to JSON format.
     */
    @Test
    void convertBookToJson() {
        String jsonResult = book.toJson();

        // Then the JSON string should accurately represent the book's state
        String expectedJson = "{\"isCheckedOut\":false,\"borrowedId\":\"\",\"author\":\"Leo Tolstoy\",\"title\":\"War and Peace\",\"bookId\":\"002\"}";
        assertEquals(expectedJson, jsonResult);
    }

    /**
     * Tests the setting and getting of the borrowedId.
     */
    @Test
    void setAndGetBorrowedId() {
        book.setBorrowedId("12345");
        assertEquals("12345", book.getBorrowedId());
    }
}
