package com.CS5031P2.frontend.controller;

import com.CS5031P2.backend.model.Book;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for handling HTTP requests related to book return operations.
 */
public class ReturnController extends HttpController {

    /**
     * Retrieves a list of borrowed books by a specific member.
     *
     * @param memberId The ID of the member for whom the borrowed books are to be retrieved.
     * @return A list of Book objects representing the borrowed books by the member.
     */
    public static List<Book> getBorrowedBooksByMember(String memberId) {
        String response = HttpController.executeGetRequest(baseUrl + "/borrowed/" + memberId);
        if (response == null || response.isEmpty() || !response.trim().startsWith("[")) {
            return new ArrayList<>();
        }
        JSONArray booksArray = new JSONArray(response);
        List<Book> borrowedBooks = new ArrayList<>();
        for (int i = 0; i < booksArray.length(); i++) {
            borrowedBooks.add(Book.fromJson(booksArray.getJSONObject(i).toString()));
        }
        return borrowedBooks;
    }

    /**
     * Initiates the process of returning a borrowed book by a specific member.
     *
     * @param memberId The ID of the member who is returning the book.
     * @param bookId   The ID of the book to be returned.
     */
    public static void returnBook(String memberId, String bookId) {
        HttpController.executeDeleteRequest(baseUrl + "/checkout/" + memberId + "/" + bookId);
    }
}
