package com.CS5031P2.frontend.controller;

import java.util.ArrayList;
import java.util.List;

import com.CS5031P2.backend.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Controller class responsible for handling HTTP requests related to book management.
 */
public class BookController extends HttpController {

    private static final String path = "/books";

    /**
     * Adds a new book to the library.
     *
     * @param title  The title of the new book.
     * @param author The author of the new book.
     */
    public static void addBook(String title, String author) {
        JSONObject bookJson = new JSONObject();
        bookJson.put("title", title);
        bookJson.put("author", author);
        executePostRequest(baseUrl + path, bookJson.toString());
    }

    /**
     * Removes a book from the library.
     *
     * @param bookId The ID of the book to be removed.
     */
    public static void removeBook(String bookId) {
        executeDeleteRequest(baseUrl + path + "/" + bookId);
    }

    /**
     * Updates information about a book in the library.
     *
     * @param bookId         The ID of the book to be updated.
     * @param newTitle       The new title of the book.
     * @param newAuthor      The new author of the book.
     */
    public static void updateBook(String bookId, String newTitle, String newAuthor) {
        JSONObject bookJson = new JSONObject();
        bookJson.put("title", newTitle);
        bookJson.put("author", newAuthor);
        executePutRequest(baseUrl + path + "/" + bookId, bookJson.toString());
    }

    /**
     * Retrieves a list of all books in the library.
     *
     * @return A list of Book objects representing all books in the library.
     */
    public static List<Book> getAllBooks() {
        String response = executeGetRequest(baseUrl + path);
        if (response == null || response.isEmpty() || !response.trim().startsWith("[")) {
            return new ArrayList<>();
        }
        JSONArray booksArray = new JSONArray(response);
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < booksArray.length(); i++) {
            books.add(Book.fromJson(booksArray.getJSONObject(i).toString()));
        }
        return books;
    }
}
