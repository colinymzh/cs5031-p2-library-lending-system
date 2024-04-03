package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.LibraryLendingSystem;
import com.CS5031P2.backend.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller class responsible for handling HTTP requests related to book checkout operations.
 */
@RestController
public class CheckOutController {

    @Autowired
    LibraryLendingSystem libraryLendingSystem;

    /**
     * Handles the HTTP POST request to check out a book for a specific member.
     *
     * @param memberId The ID of the member who is checking out the book.
     * @param bookId   The ID of the book to be checked out.
     * @return A ResponseEntity indicating the result of the checkout operation.
     *         If successful, returns an OK response with a message confirming the checkout.
     *         If the book or member is not found, returns a Not Found response.
     */
    @PostMapping("/checkout/{memberId}/{bookId}")
    public ResponseEntity<String> checkOutBook(@PathVariable String memberId, @PathVariable String bookId) {
        if (libraryLendingSystem.checkOutBook(memberId, bookId)) {
            Optional<Member> member = libraryLendingSystem.getMember(memberId);
            Optional<Book> book = libraryLendingSystem.getBook(bookId);

            if (member.isPresent() && book.isPresent()) {
                return ResponseEntity.ok("Book " + book.get().getTitle() + " successfully checked out by member " + member.get().getName());
            }

        }

        return ResponseEntity.notFound().build(); // Book not found
    }
}

