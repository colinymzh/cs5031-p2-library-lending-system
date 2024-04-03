package com.CS5031P2.frontend.controller;

/**
 * Controller class responsible for handling HTTP requests related to book checkout operations.
 */
public class CheckOutController extends HttpController {

    protected static String path = "/checkout";

    /**
     * Initiates the process of checking out a book for a specific member.
     *
     * @param memberId The ID of the member who is checking out the book.
     * @param bookId   The ID of the book to be checked out.
     */
    public static void checkOutBook(String memberId, String bookId) {
        executePostRequest(baseUrl + path + "/" + memberId + "/" + bookId, "{}");
    }
}
