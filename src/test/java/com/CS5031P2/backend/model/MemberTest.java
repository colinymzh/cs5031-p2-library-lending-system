package com.CS5031P2.backend.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Member class.
 */
public class MemberTest {

    /**
     * Test constructor and getters.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange
        Member member = new Member("John Doe", "123 Main St");

        // Act & Assert
        assertEquals("John Doe", member.getName());
        assertEquals("123 Main St", member.getAddress());
    }

    /**
     * Test setters and getters.
     */
    @Test
    public void testSettersAndGetters() {
        // Arrange
        Member member = new Member("Jane Smith", "456 Oak St");
        member.setMemberId("1");
        member.setName("John Doe");
        member.setAddress("123 Main St");

        // Act & Assert
        assertEquals("1", member.getMemberId());
        assertEquals("John Doe", member.getName());
        assertEquals("123 Main St", member.getAddress());
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        // Arrange
        Member member = new Member("John Doe", "123 Main St");
        String expectedToString = "Member(memberId="
                + member.getMemberId()
                + ", name="
                + member.getName()
                + ", address="
                + member.getAddress()
                + ", borrowedBooks="
                + member.getBorrowedBooks()
                + ")";

        // Act & Assert
        assertEquals(expectedToString, member.toString());
    }

    /**
     * Test fromJson method.
     */
    @Test
    public void testFromJson() {
        // Arrange
        String jsonStr = "{\"memberId\":\"123456\",\"name\":\"John Doe\",\"address\":\"123 Main St\",\"borrowedBooks\":{\"001\":{\"bookId\":\"001\",\"borrowedId\":\"123456\",\"title\":\"1984\",\"author\":\"George Orwell\",\"checkedOut\":true}}}";

        // Act
        Member member = Member.fromJson(jsonStr);

        // Assert
        assertNotNull(member);
        assertEquals("123456", member.getMemberId());
        assertEquals("John Doe", member.getName());
        assertEquals("123 Main St", member.getAddress());
        assertEquals(1, member.getBorrowedBooks().size());
    }

    /**
     * Test fromJson method without borrowed books.
     */
    @Test
    public void testFromJsonWithoutBorrowedBooks() {
        // Arrange
        String jsonStr = "{\"memberId\":\"789012\",\"name\":\"Jane Doe\",\"address\":\"456 Elm St\",\"borrowedBooks\":{}}";

        // Act
        Member member = Member.fromJson(jsonStr);

        // Assert
        assertNotNull(member);
        assertEquals("789012", member.getMemberId());
        assertEquals("Jane Doe", member.getName());
        assertEquals("456 Elm St", member.getAddress());

        // Verify that borrowedBooks is handled correctly when it is missing
        assertTrue(member.getBorrowedBooks() == null || member.getBorrowedBooks().isEmpty());
    }

    /**
     * Test fromJson method with null borrowed books.
     */
    @Test
    public void testFromJsonWithNullBorrowedBooks() {
        // Arrange
        String jsonStr = "{\"memberId\":\"123456\",\"name\":\"John Doe\",\"address\":\"123 Main St\", \"borrowedBooks\":null}";

        // Act
        Member member = Member.fromJson(jsonStr);

        // Assert
        assertNotNull(member);
        assertEquals("123456", member.getMemberId());
        assertEquals("John Doe", member.getName());
        assertEquals("123 Main St", member.getAddress());
        assertTrue(member.getBorrowedBooks().isEmpty());
    }


    /**
     * Test toJson method.
     */
    @Test
    public void testToJson() {
        // Arrange
        Member member = new Member("Jane Doe", "456 Elm St");
        member.setMemberId("789012");
        Map<String, Book> borrowedBooks = new HashMap<>();
        member.setBorrowedBooks((HashMap<String, Book>) borrowedBooks);

        // Act
        String jsonStr = member.toJson();

        // Assert
        String expectedJson = "{\"address\":\"456 Elm St\",\"borrowedBooks\":\"{}\",\"name\":\"Jane Doe\",\"memberId\":\"789012\"}";
        assertEquals(expectedJson, jsonStr);
    }

    /**
     * Test setBorrowedBooks method.
     */
    @Test
    public void testSetBorrowedBooks() {
        // Arrange
        Member member = new Member("Jane Doe", "456 Elm St");
        Map<String, Book> borrowedBooks = new HashMap<>();
        Book book1 = new Book("Title1", "Author1");
        Book book2 = new Book("Title2", "Author2");
        borrowedBooks.put("book1", book1);
        borrowedBooks.put("book2", book2);

        // Act
        member.setBorrowedBooks((HashMap<String, Book>) borrowedBooks);

        // Assert
        assertEquals(2, member.getBorrowedBooks().size());
        assertEquals(book1, member.getBorrowedBooks().get("book1"));
        assertEquals(book2, member.getBorrowedBooks().get("book2"));
    }


}
