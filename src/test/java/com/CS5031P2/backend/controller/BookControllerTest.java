package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.controller.BookController;
import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.LibraryLendingSystem;
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
 * This class contains test cases for the BookController class.
 */
@SpringBootTest
class BookControllerTest {

    @Autowired
    private BookController bookController;

    @MockBean
    private LibraryLendingSystem libraryLendingSystem;

    /**
     * Test case to verify that BookController.getAllBooks() returns a list of books.
     */
    @Test
    void getAllBooksReturnsBooksList() {
        // Arrange
        List<Book> books = new ArrayList<>();
        books.add(new Book("Book1", "Author1"));
        books.add(new Book("Book2", "Author2"));
        books.get(0).setBookId("1");
        books.get(1).setBookId("2");

        when(libraryLendingSystem.getBooks()).thenReturn(createBookMap(books));

        // Act
        ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(books, responseEntity.getBody());
    }

    /**
     * Test case to verify that BookController.getAllBooks() returns HttpStatus.NOT_FOUND when the book list is empty.
     */
    @Test
    void getAllBooksReturnsNotFoundForEmptyBookList() {
        // Arrange
        LibraryLendingSystem libraryLendingSystemMock = mock(LibraryLendingSystem.class);
        when(libraryLendingSystemMock.getBooks()).thenReturn(Collections.emptyMap());

        BookController bookController = new BookController();
        bookController.libraryLendingSystem = libraryLendingSystemMock;

        // Act
        ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(libraryLendingSystemMock, times(1)).getBooks(); // Verify that getBooks() is called
    }

    /**
     * Test case to verify that BookController.updateBook(String, Book) returns HttpStatus.OK when the book exists.
     */
    @Test
    void updateBookReturnsOkWhenBookExists() {
        // Arrange
        String bookId = "123";
        Book updatedBook = new Book("UpdatedBook", "UpdatedAuthor");

        when(libraryLendingSystem.getBook(bookId)).thenReturn(Optional.of(BookControllerTest.createSingleBook(bookId)));
        doNothing().when(libraryLendingSystem).updateBook(bookId, updatedBook);

        // Act
        ResponseEntity<String> responseEntity = bookController.updateBook(bookId, updatedBook);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book updated successfully", responseEntity.getBody());
    }

    /**
     * Test case to verify that BookController.updateBook(String, Book) returns HttpStatus.NOT_FOUND when the book does not exist.
     */
    @Test
    void updateBookReturnsNotFoundWhenBookDoesNotExist() {
        // Arrange
        String nonExistingBookId = "456";
        Book updatedBook = new Book("UpdatedBook", "UpdatedAuthor");

        when(libraryLendingSystem.getBook(nonExistingBookId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> responseEntity = bookController.updateBook(nonExistingBookId, updatedBook);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Test case to verify that BookController.addBook(Book) returns HttpStatus.CREATED.
     */
    @Test
    void addBookReturnsCreated() {
        // Arrange
        Book newBook = new Book("NewBook", "NewAuthor");

        doNothing().when(libraryLendingSystem).addBook(newBook.getTitle(), newBook.getAuthor());

        // Act
        ResponseEntity<String> responseEntity = bookController.addBook(newBook);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Book added Successfully", responseEntity.getBody());
    }

    /**
     * Test case to verify that BookController.deleteBook(String) returns HttpStatus.OK.
     */
    @Test
    void deleteBookReturnsOk() {
        // Arrange
        String bookId = "789";

        doNothing().when(libraryLendingSystem).deleteBook(bookId);

        // Act
        ResponseEntity<String> responseEntity = bookController.deleteBook(bookId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book Deleted Successfully", responseEntity.getBody());
    }

    /**
     * Helper method to create a map of books.
     *
     * @param books The list of books.
     * @return A map of books.
     */
    private static Map<String, Book> createBookMap(List<Book> books) {
        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            bookMap.put(book.getBookId(), book);
        }
        return bookMap;
    }

    /**
     * Helper method to create a single book.
     *
     * @param bookId The ID of the book.
     * @return A book object.
     */
    private static Book createSingleBook(String bookId) {
        return new Book("TestBook", "TestAuthor");
    }
}
