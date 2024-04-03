package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.Book;
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
 * This class contains test cases for the ReturnController class.
 */
@SpringBootTest
class ReturnControllerTest {

    @Autowired
    private ReturnController returnController;

    @MockBean
    private LibraryLendingSystem libraryLendingSystem;

    /**
     * Test case to verify that ReturnController.returnBook (GET) returns a list of borrowed books when a member exists.
     */
    @Test
    void getBorrowedBooksReturnsBooksListWhenMemberExists() {
        // Arrange
        String memberId = "member1";
        List<Book> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(new Book("Book1", "Author1"));
        borrowedBooks.add(new Book("Book2", "Author2"));
        Member member = new Member("John Doe", "123 Main St");

        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.of(member));
        when(libraryLendingSystem.getBorrowedBooks(memberId)).thenReturn(createBookMap(borrowedBooks));

        // Act
        ResponseEntity<List> responseEntity = returnController.returnBook(memberId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(borrowedBooks.size(), responseEntity.getBody().size());
    }

    /**
     * Test case to verify that ReturnController.returnBook (GET) returns HttpStatus.NOT_FOUND when the member does not exist.
     */
    @Test
    void getBorrowedBooksReturnsNotFoundWhenMemberDoesNotExist() {
        // Arrange
        String nonExistingMemberId = "nonMember";

        when(libraryLendingSystem.getMember(nonExistingMemberId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List> responseEntity = returnController.returnBook(nonExistingMemberId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Test case to verify that ReturnController.returnBook (DELETE) returns HttpStatus.OK when the book is successfully returned.
     */
    @Test
    void returnBookReturnsOkWhenSuccessful() {
        // Arrange
        String memberId = "member1";
        String bookId = "book1";

        when(libraryLendingSystem.returnBook(memberId, bookId)).thenReturn(true);
        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.of(new Member("Bob", "St Andrews")));
        when(libraryLendingSystem.getBook(bookId)).thenReturn(Optional.of(new Book("Harry Potter", "JK Rowling")));

        // Act
        ResponseEntity<String> responseEntity = returnController.returnBook(memberId, bookId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book Harry Potter successfully returned by member Bob", responseEntity.getBody());
    }

    /**
     * Test case to verify that ReturnController.returnBook (DELETE) returns HttpStatus.NOT_FOUND when the book or member does not exist.
     */
    @Test
    void returnBookReturnsNotFoundWhenBookOrMemberDoesNotExist() {
        // Arrange
        String nonExistingMemberId = "nonMember";
        String nonExistingBookId = "nonBook";

        when(libraryLendingSystem.returnBook(nonExistingMemberId, nonExistingBookId)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = returnController.returnBook(nonExistingMemberId, nonExistingBookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Helper method to create a map of books for testing.
     *
     * @param books The list of books.
     * @return A map with book IDs as keys and Book objects as values.
     */
    private static Map<String, Book> createBookMap(List<Book> books) {
        Map<String, Book> bookMap = new HashMap<>();
        for (Book book : books) {
            bookMap.put(book.getBookId(), book);
        }
        return bookMap;
    }
}
