package com.CS5031P2.backend.util;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.*;

import com.CS5031P2.backend.model.Book;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Map;

/**
 * Unit tests for the BookParser class.
 */
public class BookParserTest {

    private BookParser bookParser;
    private static MockedStatic<FileUtil> mockedFileUtil;

    /**
     * Setup mock behavior before running any tests.
     * @throws IOException if an I/O error occurs
     */
    @BeforeAll
    public static void mockSetup() throws IOException {
        mockedFileUtil = Mockito.mockStatic(FileUtil.class);
        when(FileUtil.readFile("src/test/resources/test_data.json"))
                .thenReturn("[{\"title\":\"Book Title\",\"author\":\"Author Name\"}]");

        // Throw IOException for a specific test file to simulate read error
        when(FileUtil.readFile("src/test/resources/invalid_data.json")).thenThrow(IOException.class);
    }

    /**
     * Clean up after running all tests.
     */
    @AfterAll
    public static void tearDown() {
        mockedFileUtil.close(); // Unregister the mock
    }

    /**
     * Setup before each test.
     */
    @BeforeEach
    public void setUp() {
        bookParser = new BookParser();
    }

    /**
     * Test successful parsing of valid data.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSuccessfulParsing() throws IOException {
        Map<String, Book> books = bookParser.parseBooks("src/test/resources/test_data.json");

        assertNotNull("The returned map should not be null", books);
        assertFalse("The map should contain at least one entry", books.isEmpty());
        assertTrue("The map should contain a book with the specified title",
                books.values().stream().anyMatch(book -> "Book Title".equals(book.getTitle())));
        assertTrue("The map should contain a book with the specified author",
                books.values().stream().anyMatch(book -> "Author Name".equals(book.getAuthor())));
    }

    /**
     * Test that an IOException is thrown when parsing invalid data.
     */
    @Test
    public void testIOExceptionThrown() {
        // Test IOException is thrown when reading an invalid file
        assertThrows(IOException.class, () -> {
            bookParser.parseBooks("src/test/resources/invalid_data.json");
        }, "IOException should be thrown when reading an invalid file");
    }
}
