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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This class contains test cases for the CheckOutController class.
 */
@SpringBootTest
class CheckOutControllerTest {

    @Autowired
    private CheckOutController checkOutController;

    @MockBean
    private LibraryLendingSystem libraryLendingSystem;

    /**
     * Test case to verify that CheckOutController.checkOutBook() returns HttpStatus.OK when the checkout is successful.
     */
    @Test
    void checkOutBookReturnsOkWhenSuccessful() {
        // Arrange
        String memberId = "member1";
        String bookId = "book1";

        when(libraryLendingSystem.checkOutBook(memberId, bookId)).thenReturn(true);
        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.of(new Member("Bob", "St Andrews")));
        when(libraryLendingSystem.getBook(bookId)).thenReturn(Optional.of(new Book("Harry Potter", "JK Rowling")));

        // Act
        ResponseEntity<String> responseEntity = checkOutController.checkOutBook(memberId, bookId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book Harry Potter successfully checked out by member Bob" , responseEntity.getBody());
    }

    /**
     * Test case to verify that CheckOutController.checkOutBook() returns HttpStatus.NOT_FOUND when libraryLendingSystem.checkOutBook
     * fails.
     */
    @Test
    void checkOutBookReturnsNotFoundWhenCheckOutBookFails() {
        // Arrange
        String nonExistingMemberId = "nonMember";
        String nonExistingBookId = "nonBook";

        when(libraryLendingSystem.checkOutBook(nonExistingMemberId, nonExistingBookId)).thenReturn(false);

        // Act
        ResponseEntity<String> responseEntity = checkOutController.checkOutBook(nonExistingMemberId, nonExistingBookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Test case to verify that CheckOutController.checkOutBook() returns HttpStatus.NOT_FOUND when the member does not exist.
     */
    @Test
    void checkOutBookReturnsNotFoundWhenMemberDoesNotExist() {
        // Arrange
        String memberId = "member1";
        String bookId = "book1";

        when(libraryLendingSystem.checkOutBook(memberId, bookId)).thenReturn(true);
        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.empty());
        when(libraryLendingSystem.getBook(bookId)).thenReturn(Optional.of(new Book("Harry Potter", "JK Rowling")));

        // Act
        ResponseEntity<String> responseEntity = checkOutController.checkOutBook(memberId, bookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    /**
     * Test case to verify that CheckOutController.checkOutBook() returns HttpStatus.NOT_FOUND when the book does not exist.
     */
    @Test
    void checkOutBookReturnsNotFoundWhenBookDoesNotExist() {
        // Arrange
        String memberId = "member1";
        String bookId = "book1";

        when(libraryLendingSystem.checkOutBook(memberId, bookId)).thenReturn(true);
        when(libraryLendingSystem.getMember(memberId)).thenReturn(Optional.of(new Member("Bob", "St Andrews")));
        when(libraryLendingSystem.getBook(bookId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> responseEntity = checkOutController.checkOutBook(memberId, bookId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

}
