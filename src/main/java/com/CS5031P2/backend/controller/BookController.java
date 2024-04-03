package com.CS5031P2.backend.controller;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.LibraryLendingSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for managing Book resources.
 */
@RestController
public class BookController {

    @Autowired
    LibraryLendingSystem libraryLendingSystem;

    /**
     * Retrieves all books from the library.
     * @return ResponseEntity containing a list of books if found, or not found status if the list is empty.
     */
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> books = new ArrayList<>(libraryLendingSystem.getBooks().values());

        if (!books.isEmpty()){
            return ResponseEntity.ok(books);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates information for a specific book.
     * @param bookId The ID of the book to update.
     * @param updatedBook The updated Book object.
     * @return ResponseEntity indicating success or failure of the update operation.
     */
    @PutMapping("/books/{bookId}")
    public ResponseEntity<String> updateBook(@PathVariable String bookId, @RequestBody Book updatedBook) {
        if (libraryLendingSystem.getBook(bookId).isPresent()) {
            libraryLendingSystem.updateBook(bookId, updatedBook);
            return ResponseEntity.ok("Book updated successfully");
        }

        return ResponseEntity.notFound().build(); // Book not found
    }

    /**
     * Adds a new book to the library.
     * @param book The Book object to add.
     * @return ResponseEntity indicating success or failure of the add operation.
     */
    @PostMapping("/books")
    public ResponseEntity<String> addBook(@RequestBody Book book){
        libraryLendingSystem.addBook(book.getTitle(), book.getAuthor());
        return ResponseEntity.status(HttpStatus.CREATED).body("Book added Successfully");
    }

    /**
     * Deletes a book from the library.
     * @param bookId The ID of the book to delete.
     * @return ResponseEntity indicating success or failure of the delete operation.
     */
    @DeleteMapping("books/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId){
        libraryLendingSystem.deleteBook(bookId);
        return ResponseEntity.ok("Book Deleted Successfully");
    }
}
