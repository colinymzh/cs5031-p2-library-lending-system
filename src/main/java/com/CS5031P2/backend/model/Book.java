package com.CS5031P2.backend.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Represents a book in the library.
 */
public class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean isCheckedOut;
    private String borrowedId;

    /**
     * Constructs a new Book instance with the specified attributes.
     *
     * @param title       The title of the book.
     * @param author      The author of the book.
     */
    public Book(String title, String author) {
        this.bookId = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isCheckedOut = false;
        this.borrowedId = "";
    }

    /**
     * Gets the unique identifier of the book.
     *
     * @return The book's unique identifier.
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Sets the unique identifier of the book.
     *
     * @param id The new unique identifier for the book.
     */
    public void setBookId(String id) {
        this.bookId = id;
    }

    /**
     * Gets the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the book.
     *
     * @param title The new title for the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author of the book.
     *
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the book.
     *
     * @param authorName The new author for the book.
     */
    public void setAuthor(String authorName) {
        this.author = authorName;
    }

    /**
     * Checks if the book is currently checked out.
     *
     * @return True if the book is checked out, false otherwise.
     */
    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    /**
     * Sets the checked-out status of the book.
     *
     * @param checkedOut True if the book is checked out, false otherwise.
     */
    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
    }

    /**
     * Parses a JSON string and creates a new Book object.
     *
     * @param jsonStr The JSON string representing the Book object.
     * @return A Book object parsed from the provided JSON string.
     * @throws JSONException If there is an error parsing the JSON string.
     */
    public static Book fromJson(String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        String bookId = jsonObject.getString("bookId");
        String title = jsonObject.getString("title");
        String author = jsonObject.getString("author");
        boolean isCheckedOut = jsonObject.getBoolean("checkedOut");
        String borrowedId = jsonObject.getString("borrowedId");

        Book book = new Book(title, author);
        book.setBookId(bookId);
        book.setCheckedOut(isCheckedOut);
        book.setBorrowedId(borrowedId);
        return book;
    }

    /**
     * Converts the current Book object to a JSON string.
     *
     * @return A JSON string representation of the Book object.
     */
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bookId", bookId);
        jsonObject.put("title", title);
        jsonObject.put("author", author);
        jsonObject.put("isCheckedOut", isCheckedOut);
        jsonObject.put("borrowedId", borrowedId);

        return jsonObject.toString();
    }

    /**
     * Gets the borrowedId of the book.
     */
    public String getBorrowedId() {
        return borrowedId;
    }

    /**
     * Sets the borrowedId of the book.
     *
     * @param borrowedId The ID of the member borrowing the book.
     */
    public void setBorrowedId(String borrowedId) {
        this.borrowedId = borrowedId;
    }
}
