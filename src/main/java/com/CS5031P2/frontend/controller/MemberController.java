package com.CS5031P2.frontend.controller;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.Member;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class serves as the controller for managing members in the library system.
 */
public class MemberController extends HttpController {

    private static final String path = "/members";

    /**
     * Add a new member to the library.
     * @param name The name of the new member
     * @param address The address of the new member
     */
    public static void addMember(String name, String address) {
        JSONObject memberJson = new JSONObject();
        memberJson.put("name", name);
        memberJson.put("address", address);
        executePostRequest(baseUrl + path, memberJson.toString());
    }

    /**
     * Remove a member from the library.
     * @param memberId The unique identifier of the member to be removed
     */
    public static void removeMember(String memberId) {
        executeDeleteRequest(baseUrl + path + "/" + memberId);
    }

    /**
     * Updates the details of an existing member in the library.
     * @param memberId The unique identifier of the member to be updated
     * @param newName The new name of the member
     * @param newAddress The new address of the member
     */
    public static void updateMember(String memberId, String newName, String newAddress) {
        // Get all members
        List<Member> members = getAllMembers();

        // Find member who needs to renew
        for (Member member : members) {
            if (member.getMemberId().equals(memberId)) {
                // Get the current borrowedBooks information of the target member
                Map<String, Book> borrowedBooks = member.getBorrowedBooks();

                // Create JSON objects for update information
                JSONObject memberJson = new JSONObject();
                memberJson.put("name", newName);
                memberJson.put("address", newAddress);

                // Add borrowedBooks to the JSON string
                if (!borrowedBooks.isEmpty()) {
                    JSONObject borrowedBooksJson = new JSONObject();
                    for (Map.Entry<String, Book> entry : borrowedBooks.entrySet()) {
                        // Convert each Book object to a JSONObject using its toJson method
                        JSONObject bookJson = new JSONObject(entry.getValue().toJson());
                        // Add the JSONObject to the borrowedBooksJson object
                        borrowedBooksJson.put(entry.getKey(), bookJson);
                    }
                    // Add the borrowedBooksJson object to the memberJson
                    memberJson.put("borrowedBooks", borrowedBooksJson);
                }


                executePutRequest(baseUrl + path + "/" + memberId, memberJson.toString());
                break;
            }
        }
    }

    /**
     * Retrieves a list of all members in the library.
     * @return A list of Member objects representing all members in the library
     */
    public static List<Member> getAllMembers() {
        String response = executeGetRequest(baseUrl + path);
        if (response == null || response.isEmpty() || !response.trim().startsWith("[")) {
            return new ArrayList<>();
        }
        JSONArray membersArray = new JSONArray(response);
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < membersArray.length(); i++) {
            members.add(Member.fromJson(membersArray.getJSONObject(i).toString()));
        }
        return members;
    }
}
