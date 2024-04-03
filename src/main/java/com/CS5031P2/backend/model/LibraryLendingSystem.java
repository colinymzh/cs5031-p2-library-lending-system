package com.CS5031P2.backend.model;

import com.CS5031P2.backend.util.BookParser;
import com.CS5031P2.backend.util.MemberParser;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Class representing the library lending system.
 */
@Component
public class LibraryLendingSystem {
    private Map<String, Book> books;
    private Map<String, Member> members;

    /**
     * Constructor for LibraryLendingSystem.
     * Initializes the books and members maps by parsing data from external sources.
     * @throws IOException if an I/O error occurs during parsing.
     */
    public LibraryLendingSystem() throws IOException {
        books = new BookParser().parseBooks("src/main/resources/book_examples.json");
        members = new MemberParser().getParsedMembers("src/main/resources/member_examples.json");
    }

    /**
     * Retrieves borrowed books associated with a member.
     * @param memberID The ID of the member.
     * @return A map of borrowed books, or an empty map if the member doesn't exist.
     */
    public Map<String, Book> getBorrowedBooks(String memberID) {
        if (members.containsKey(memberID)) {
            Member member = members.get(memberID);
            return member.getBorrowedBooks();
        }

        // Returns an empty hashmap if no member
        return new HashMap<>();
    }

    /**
     * Retrieves all books in the library.
     * @return A map of books.
     */
    public Map<String, Book> getBooks() {
        return books;
    }

    /**
     * Sets the books in the library.
     * @param books A map of books.
     */
    public void setBooks(Map<String, Book> books) {
        this.books = books;
    }

    /**
     * Retrieves all members in the library.
     * @return A map of members.
     */
    public Map<String, Member> getMembers() {
        return members;
    }

    /**
     * Sets the members in the library.
     * @param members A map of members.
     */
    public void setMembers(Map<String, Member> members) {
        this.members = members;
    }

    /**
     * Adds a new book to the library.
     * @param title The title of the book.
     * @param author The author of the book.
     */
    public void addBook(String title, String author){
        Book book = new Book(title, author);
        books.put(book.getBookId(), book);
    }

    /**
     * Retrieves a book by its ID.
     * @param bookId The ID of the book.
     * @return An Optional containing the book if found, or empty if not found.
     */
    public Optional<Book> getBook(String bookId) {
        return Optional.ofNullable(books.get(bookId));
    }

    /**
     * Updates information for a specific book.
     * @param bookId The ID of the book to update.
     * @param updatedBook The updated Book object.
     */
    public void updateBook(String bookId, Book updatedBook){
        if (books.containsKey(bookId)) {
            updatedBook.setBookId(bookId);
            books.put(bookId, updatedBook);
        }
    }

    /**
     * Deletes a book from the library.
     * @param bookId The ID of the book to delete.
     */
    public void deleteBook(String bookId){
        books.remove(bookId);
    }

    /**
     * Retrieves a member by their ID.
     * @param memberId The ID of the member.
     * @return An Optional containing the member if found, or empty if not found.
     */
    public Optional<Member> getMember(String memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    /**
     * Adds a new member to the library.
     * @param name The name of the member.
     * @param address The address of the member.
     */
    public void addMember(String name, String address) {
        Member member = new Member(name, address);
        members.put(member.getMemberId(), member);
    }

    /**
     * Updates information for a specific member.
     * @param memberId The ID of the member to update.
     * @param updatedMember The updated Member object.
     */
    public void updateMember(String memberId, Member updatedMember) {
        if (members.containsKey(memberId)) {
            updatedMember.setMemberId(memberId);
            members.put(memberId, updatedMember);
        }
    }

    /**
     * Deletes a member from the library.
     * @param memberId The ID of the member to delete.
     */
    public void deleteMember(String memberId) {
        members.remove(memberId);
    }

    /**
     * Checks out a book for a specific member.
     *
     * @param memberId The ID of the member who is borrowing the book.
     * @param bookId   The ID of the book to be borrowed.
     * @return True if the book was successfully checked out, false otherwise.
     */
    public boolean checkOutBook(String memberId, String bookId) {
        if (isValidBorrowerAndBook(memberId, bookId)) {
            Member member = getMember(memberId).get();
            Book book = getBook(bookId).get();

            // Check out book
            if (!book.isCheckedOut()) {
                member.borrowBook(book);
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a borrowed book.
     *
     * @param memberId The ID of the member who is returning the book.
     * @param bookId   The ID of the book to be returned.
     * @return True if the book was successfully returned, false otherwise.
     */
    public boolean returnBook(String memberId, String bookId) {
        if (isValidBorrowerAndBook(memberId, bookId)) {
            Member member = getMember(memberId).get();
            Book book = getBook(bookId).get();

            // Check if book is checked out
            if (book.isCheckedOut()) {
                Map<String, Book> borrowedBooks = getBorrowedBooks(memberId);

                // If borrowed by this user then return
                if (borrowedBooks.containsKey(bookId)) {
                    member.returnBook(book);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks whether the provided member ID and book ID are valid.
     *
     * @param memberId The ID of the member.
     * @param bookId   The ID of the book.
     * @return True if both the member and the book exist, false otherwise.
     */
    private boolean isValidBorrowerAndBook(String memberId, String bookId) {
        return getMember(memberId).isPresent() && getBook(bookId).isPresent();
    }

}
