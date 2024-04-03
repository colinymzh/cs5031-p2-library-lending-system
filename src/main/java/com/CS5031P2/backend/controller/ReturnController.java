package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.LibraryLendingSystem;
import com.CS5031P2.backend.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class responsible for handling HTTP requests related to book return operations.
 */
@RestController
public class ReturnController {

    @Autowired
    LibraryLendingSystem libraryLendingSystem;

    /**
     * Handles the HTTP GET request to retrieve a list of borrowed books by a specific member.
     *
     * @param memberId The ID of the member whose borrowed books are to be retrieved.
     * @return A ResponseEntity containing a list of borrowed books if the member is found.
     *         If the member is not found, returns a Not Found response.
     */
    @GetMapping("/borrowed/{memberId}")
    public ResponseEntity<List> returnBook(@PathVariable String memberId) {
        if (libraryLendingSystem.getMember(memberId).isPresent()) {
            List<Book> borrowedBooks = new ArrayList<>(libraryLendingSystem.getBorrowedBooks(memberId).values());
            return ResponseEntity.ok(borrowedBooks);
        }

        return ResponseEntity.notFound().build(); // Member not found
    }

    /**
     * Handles the HTTP DELETE request to return a borrowed book by a specific member.
     *
     * @param memberId The ID of the member who is returning the book.
     * @param bookId   The ID of the book to be returned.
     * @return A ResponseEntity indicating the result of the return operation.
     *         If successful, returns an OK response with a message confirming the return.
     *         If the book or member is not found, returns a Not Found response.
     */
    @DeleteMapping("/checkout/{memberId}/{bookId}")
    public ResponseEntity<String> returnBook(@PathVariable String memberId, @PathVariable String bookId) {
        if (libraryLendingSystem.returnBook(memberId, bookId)) {
            Optional<Member> member = libraryLendingSystem.getMember(memberId);
            Optional<Book> book = libraryLendingSystem.getBook(bookId);
            return ResponseEntity.ok("Book " + book.get().getTitle() + " successfully returned by member " + member.get().getName());
        }

        return ResponseEntity.notFound().build(); // Book not found
    }
}

