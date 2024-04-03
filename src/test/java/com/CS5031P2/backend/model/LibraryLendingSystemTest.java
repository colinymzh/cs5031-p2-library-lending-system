package com.CS5031P2.backend.model;

import com.CS5031P2.backend.util.FileUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.*;

/**
 * This class contains test cases for the LibraryLendingSystem class.
 */
public class LibraryLendingSystemTest {
    private LibraryLendingSystem library;
    private static MockedStatic<FileUtil> mockedFileUtil;

    @BeforeAll
    public static void mockSetup() throws IOException {
        mockedFileUtil = Mockito.mockStatic(FileUtil.class);
        mockedFileUtil.when(() -> FileUtil.readFile("src/main/resources/book_examples.json"))
                .thenReturn("[{\"title\":\"Book Title\",\"author\":\"Author Name\"}]");
        mockedFileUtil.when(() -> FileUtil.readFile("src/main/resources/member_examples.json"))
                .thenReturn("[{\"name\":\"Member Name\",\"Address\":\"Member Address\"}]");
    }

    @AfterAll
    public static void tearDown() {
        mockedFileUtil.close(); // Unregister the mock
    }

    /**
     * Set up method to initialize the library before each test.
     * @throws IOException if an I/O error occurs.
     */
    @BeforeEach
    public void setUp() throws IOException {
        library = new LibraryLendingSystem();
    }

    /**
     * Test method to verify the addition of a book to the library.
     */
    @Test
    public void testAddBook() {
        String title = "Effective Java";
        String author = "Joshua Bloch";
        library.addBook(title, author);

        assertFalse("Books map should not be empty after adding a book", library.getBooks().isEmpty());
        assertTrue("Added book should be in the library", library.getBooks().values().stream().anyMatch(book -> book.getTitle().equals(title) && book.getAuthor().equals(author)));
    }

    /**
     * Test method to verify the retrieval of borrowed books for a member.
     */
    @Test
    public void testGetBorrowedBooks() {
        String memberID = "member1";
        library.addMember("John Doe", "123 Main St");
        assertTrue("Borrowed books should be empty for a new member", library.getBorrowedBooks(memberID).isEmpty());
    }

    /**
     * Test method to verify the addition of a member to the library.
     */
    @Test
    public void testAddMember() {
        String name = "Jane Doe";
        String address = "456 Elm St";
        library.addMember(name, address);

        assertFalse("Members map should not be empty after adding a member", library.getMembers().isEmpty());
        assertTrue("Added member should be in the library", library.getMembers().values().stream().anyMatch(member -> member.getName().equals(name) && member.getAddress().equals(address)));
    }

    /**
     * Test method to verify the update of a book in the library.
     */
    @Test
    public void testUpdateBook() {
        // Add a book to the system
        library.addBook("Old Title", "Old Author");

        // Retrieve the bookID
        Optional<String> bookIdOptional = library.getBooks().entrySet().stream()
                .filter(entry -> "Old Title".equals(entry.getValue().getTitle()) && "Old Author".equals(entry.getValue().getAuthor()))
                .map(Map.Entry::getKey)
                .findFirst();

        assertTrue("BookId should be present", bookIdOptional.isPresent());
        String bookId = bookIdOptional.get();

        // Create an updated book
        Book updatedBook = new Book("New Title", "New Author");

        // Update the book in the library
        library.updateBook(bookId, updatedBook);

        // Assertions to verify the book has been updated
        assertEquals("Book should be updated with new title", "New Title", library.getBook(bookId).get().getTitle());
        assertEquals("Book should be updated with new author", "New Author", library.getBook(bookId).get().getAuthor());
    }

    /**
     * Test method to verify the deletion of a book from the library.
     */
    @Test
    public void testDeleteBook() {
        String bookId = "book1";
        library.addBook("Some Title", "Some Author");
        library.deleteBook(bookId);

        assertFalse("Deleted book should not be found", library.getBook(bookId).isPresent());
    }

    /**
     * Test method to verify the update of a member in the library.
     */
    @Test
    public void testUpdateMember() {
        // Add a member to the system
        library.addMember("Old Name", "Old Address");

        // Retrieve the memberID
        Optional<String> memberIdOptional = library.getMembers().entrySet().stream()
                .filter(entry -> "Old Name".equals(entry.getValue().getName()) && "Old Address".equals(entry.getValue().getAddress()))
                .map(Map.Entry::getKey)
                .findFirst();

        assertTrue("MemberId should be present", memberIdOptional.isPresent());
        String memberId = memberIdOptional.get();

        // Create an updated member with the new details
        Member updatedMember = new Member("New Name", "New Address");

        // Update the member in the library using the retrieved memberId
        library.updateMember(memberId, updatedMember);

        // Assertions to verify the member has been updated
        assertEquals("Member should be updated with new name", "New Name", library.getMember(memberId).get().getName());
        assertEquals("Member should be updated with new address", "New Address", library.getMember(memberId).get().getAddress());
    }

    /**
     * Test method to verify the deletion of a member from the library.
     */
    @Test
    public void testDeleteMember() {
        String memberId = "member1";
        library.addMember("Some Name", "Some Address");
        library.deleteMember(memberId);

        assertFalse("Deleted member should not be found", library.getMember(memberId).isPresent());
    }

    /**
     * Test method to verify the replacement of the books map with a new map.
     */
    @Test
    public void testSetBooks() {
        Map<String, Book> newBooks = new HashMap<>();
        Book newBook = new Book("New Title", "New Author");
        newBooks.put("newBookId", newBook);

        library.setBooks(newBooks);
        assertEquals("Books map should be replaced with the new map", newBooks, library.getBooks());
        assertTrue("New book should be in the updated map", library.getBooks().containsKey("newBookId"));
    }

    /**
     * Test method to verify the replacement of the members map with a new map.
     */
    @Test
    public void testSetMembers() {
        Map<String, Member> newMembers = new HashMap<>();
        Member newMember = new Member("New Name", "New Address");
        newMembers.put("newMemberId", newMember);

        library.setMembers(newMembers);
        assertEquals("Members map should be replaced with the new map", newMembers, library.getMembers());
        assertTrue("New member should be in the updated map", library.getMembers().containsKey("newMemberId"));
    }

    /**
     * Test method to verify the update of a non-existent book.
     */
    @Test
    public void testUpdateNonExistentBook() {
        String nonExistentBookId = "nonExistentBookId";
        Book updatedBook = new Book("New Title", "New Author");

        // Attempt to update a non-existent book
        library.updateBook(nonExistentBookId, updatedBook);

        // Verify the book was not added to the library
        assertFalse("Non-existent book should not be added", library.getBook(nonExistentBookId).isPresent());
    }

    /**
     * Test method to verify the update of a non-existent member.
     */
    @Test
    public void testUpdateNonExistentMember() {
        String nonExistentMemberId = "nonExistentMemberId";
        Member updatedMember = new Member("New Name", "New Address");

        // Attempt to update a non-existent member
        library.updateMember(nonExistentMemberId, updatedMember);

        // Verify the member was not added to the members map
        assertFalse("Non-existent member should not be added", library.getMember(nonExistentMemberId).isPresent());
    }

    /**
     * Test method to verify checking out a book by a member.
     */
    @Test
    public void testCheckOutBook() {
        // Setup: Add a member and a book
        String name = "John Doe";
        String address = "123 Main St";
        library.addMember(name, address);
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);

        String title = "Effective Java";
        String author = "Joshua Bloch";
        library.addBook(title, author);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);

        // Action: Member checks out a book
        boolean checkOutSuccess = library.checkOutBook(memberID, bookID);

        // Verify: The book is checked out successfully
        assertTrue("Book should be checked out successfully", checkOutSuccess);
        assertTrue("Book's checked out status should be true", library.getBook(bookID).get().isCheckedOut());
        assertFalse("Member should have borrowed books", library.getBorrowedBooks(memberID).isEmpty());
    }

    /**
     * Test method to verify returning a book by a member.
     */
    @Test
    public void testReturnBook() {
        // Setup: Add a member and a book, and check out the book
        String name = "Jane Doe";
        String address = "456 Elm St";
        library.addMember(name, address);
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);

        String title = "Clean Code";
        String author = "Robert C. Martin";
        library.addBook(title, author);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);

        library.checkOutBook(memberID, bookID); // Member checks out the book

        // Action: Member returns the book
        boolean returnSuccess = library.returnBook(memberID, bookID);

        // Verify: The book is returned successfully
        assertTrue("Book should be returned successfully", returnSuccess);
        assertFalse("Book's checked out status should be false", library.getBook(bookID).get().isCheckedOut());
        assertTrue("Member should have no borrowed books", library.getBorrowedBooks(memberID).isEmpty());
    }

    /**
     * Test method to verify failure to check out a book that is already checked out.
     */
    @Test
    public void testCheckOutAlreadyCheckedOutBook() {
        // Setup: Add a member and a book, and check out the book
        library.addMember("Member One", "Address One");
        library.addBook("Book One", "Author One");
        String memberID1 = new ArrayList<>(library.getMembers().keySet()).get(0);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);

        library.checkOutBook(memberID1, bookID); // First member checks out the book

        // Attempt: Another member tries to check out the same book
        library.addMember("Member Two", "Address Two");
        String memberID2 = new ArrayList<>(library.getMembers().keySet()).get(1);
        boolean checkOutSuccess = library.checkOutBook(memberID2, bookID);

        // Verify: The second member cannot check out the already checked out book
        assertFalse("Should not be able to check out an already checked out book", checkOutSuccess);
    }

    /**
     * Test method to verify failure to return a book not checked out by the member.
     */
    @Test
    public void testReturnBookNotCheckedOutByMember() {
        // Setup: Add a member and a book, but do not check out the book
        library.addMember("Member One", "Address One");
        library.addBook("Book One", "Author One");
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);

        // Attempt: Member tries to return a book that they haven't checked out
        boolean returnSuccess = library.returnBook(memberID, bookID);

        // Verify: The member cannot return a book that they haven't checked out
        assertFalse("Should not be able to return a book not checked out by the member", returnSuccess);
    }

    /**
     * Test method to verify failure to check out a book with a non-existent member.
     */
    @Test
    public void testCheckOutBookWithNonExistentMember() {
        // Setup: Add only a book to the library, no member
        String title = "Refactoring";
        String author = "Martin Fowler";
        library.addBook(title, author);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);

        // Attempt: Non-existent member tries to check out the book
        String nonExistentMemberID = "nonExistentMember";
        boolean checkOutSuccess = library.checkOutBook(nonExistentMemberID, bookID);

        // Verify: Non-existent member cannot check out the book
        assertFalse("Non-existent member should not be able to check out a book", checkOutSuccess);
    }

    /**
     * Test method to verify failure to check out a non-existent book.
     */
    @Test
    public void testCheckOutNonExistentBook() {
        // Setup: Add only a member to the library, no book
        String name = "Tom Sawyer";
        String address = "Mississippi River";
        library.addMember(name, address);
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);

        // Attempt: Member tries to check out a non-existent book
        String nonExistentBookID = "nonExistentBook";
        boolean checkOutSuccess = library.checkOutBook(memberID, nonExistentBookID);

        // Verify: Member cannot check out a non-existent book
        assertFalse("Member should not be able to check out a non-existent book", checkOutSuccess);
    }

    /**
     * Test method to verify failure to return a book with a non-existent member.
     */
    @Test
    public void testReturnBookWithNonExistentMember() {
        // Setup: Add a book and check it out to a valid member
        library.addMember("Mark Twain", "Missouri");
        library.addBook("Adventures of Huckleberry Finn", "Mark Twain");
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);
        library.checkOutBook(memberID, bookID); // Valid member checks out the book

        // Attempt: Non-existent member tries to return the book
        String nonExistentMemberID = "nonExistentMember";
        boolean returnSuccess = library.returnBook(nonExistentMemberID, bookID);

        // Verify: Non-existent member cannot return the book
        assertFalse("Non-existent member should not be able to return a book", returnSuccess);
    }

    /**
     * Test method to verify failure to return a non-existent book.
     */
    @Test
    public void testReturnNonExistentBook() {
        // Setup: Add a member to the library, no book
        library.addMember("Herman Melville", "New York");
        String memberID = new ArrayList<>(library.getMembers().keySet()).get(0);

        // Attempt: Member tries to return a non-existent book
        String nonExistentBookID = "nonExistentBook";
        boolean returnSuccess = library.returnBook(memberID, nonExistentBookID);

        // Verify: Member cannot return a non-existent book
        assertFalse("Member should not be able to return a non-existent book", returnSuccess);
    }

    /**
     * Test method to verify failure to return a book that is borrowed by another member.
     */
    @Test
    public void testReturnBookBorrowedByAnotherMember() {
        // Setup: Add two members and one book
        library.addMember("Mark Twain", "America");
        library.addMember("Charles Dickens", "London");
        library.addBook("Harry Potter", "JK Rowling");

        // Member One borrows the book
        String memberOneID = new ArrayList<>(library.getMembers().keySet()).get(0);
        String bookID = new ArrayList<>(library.getBooks().keySet()).get(0);
        library.checkOutBook(memberOneID, bookID); // Member One checks out the book

        // Attempt: Member Two tries to return the book borrowed by Member One
        String memberTwoID = new ArrayList<>(library.getMembers().keySet()).get(1);
        boolean returnSuccess = library.returnBook(memberTwoID, bookID);

        // Verify: Member Two cannot return a book that is borrowed by Member One
        assertFalse("A member should not be able to return a book borrowed by another member", returnSuccess);
    }



}
