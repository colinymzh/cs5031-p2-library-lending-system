package com.CS5031P2.backend.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a member entity with basic information such as ID, name, and address.
 * This class provides methods for serialization and deserialization to/from JSON format.
 */
public class Member {
    private String memberId;
    private String name;
    private String address;
    private HashMap<String, Book> borrowedBooks;

    /**
     * Constructs a new Member object with the specified details.
     * @param name The name of the member
     * @param address The address of the member
     */
    public Member(String name, String address) {
        this.memberId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.borrowedBooks = new HashMap<>();
    }

    /**
     * Parses a JSON string and returns a corresponding Member object.
     * @param jsonStr The JSON string representing member information
     * @return The Member object parsed from the JSON string
     */
    public static Member fromJson(String jsonStr) {
        JSONObject jsonObject = new JSONObject(jsonStr);
        String identifier = jsonObject.getString("memberId");
        String name = jsonObject.getString("name");
        String address = jsonObject.getString("address");

        Member member = new Member(name, address);
        member.setMemberId(identifier);

        // Check if borrowedBooks exists
        if (jsonObject.has("borrowedBooks") && !jsonObject.isNull("borrowedBooks")) {
            JSONObject borrowedBooksObject = jsonObject.getJSONObject("borrowedBooks");
            JSONArray bookIds = borrowedBooksObject.names();
            if (bookIds != null) {
                for (int i = 0; i < bookIds.length(); i++) {
                    String bookId = bookIds.getString(i);
                    JSONObject bookObject = borrowedBooksObject.getJSONObject(bookId);
                    Book book = Book.fromJson(bookObject.toString());
                    member.borrowBook(book);
                }
            }
        }

        return member;
    }

    /**
     * Serializes the Member object to a JSON string.
     * @return The JSON string representing the Member object
     */
    public String toJson() {
        JSONObject memberJson = new JSONObject();
        memberJson.put("memberId", this.memberId);
        memberJson.put("name", this.name);
        memberJson.put("address", this.address);
        memberJson.put("borrowedBooks", this.borrowedBooks.toString());
        return memberJson.toString();
    }

    /**
     * Returns a string representation of the Member object, including its member ID, name, address,
     * and a list of borrowed books.
     *
     * @return A string representation of the Member object.
     */
    @Override
    public String toString() {
        String id = "memberId=" + this.memberId;
        String name = ", name=" + this.name;
        String address = ", address=" + this.address;
        String borrowed = ", borrowedBooks=" + this.borrowedBooks.toString();
        return "Member(" + id + name + address + borrowed + ")";
    }


    /**
     * Gets the ID of the member.
     * @return The ID of the member
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * Sets the ID of the member.
     * @param memberId The ID of the member
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * Gets the name of the member.
     * @return The name of the member
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the member.
     * @param name The name of the member
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the address of the member.
     * @return The address of the member
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the member.
     * @param address The address of the member
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Retrieves a map of borrowed books, where the key is the book ID and the value is the corresponding Book object.
     *
     * @return A map containing the borrowed books.
     */
    public Map<String, Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     * Sets the map of borrowed books.
     *
     * @param borrowedBooks A HashMap containing the borrowed books, where the key is the book ID and the value is the corresponding Book object.
     */
    public void setBorrowedBooks(HashMap<String, Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    /**
     * Borrows a book and adds it to the borrowed books map. Also sets the book's checked out status to true.
     *
     * @param book The Book object to be borrowed.
     */
    public void borrowBook(Book book) {
        borrowedBooks.put(book.getBookId(), book);
        book.setCheckedOut(true);
        book.setBorrowedId(this.memberId);
    }

    /**
     * Returns a borrowed book and removes it from the borrowed books map. Also sets the book's checked out status to false.
     *
     * @param book The Book object to be returned.
     */
    public void returnBook(Book book) {
        borrowedBooks.remove(book.getBookId());
        book.setCheckedOut(false);
        book.setBorrowedId("");
    }

}
