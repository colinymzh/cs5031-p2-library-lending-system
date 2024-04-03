package com.CS5031P2.frontend.controller;
import com.CS5031P2.frontend.MainFrame;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.CS5031P2.frontend.MainFrame.overlay;

/**
 * This class provides utility methods for making HTTP requests to the backend server.
 */
public class HttpController {
    private static HttpClient httpClient = HttpClient.newHttpClient();
    protected static String baseUrl = "http://localhost:8080";

    // Add a setter for HttpClient to allow injection in tests
    public static void setHttpClient(HttpClient client) {
        httpClient = client;
    }

    /**
     * Executes an HTTP POST request to the specified URL with the given JSON payload.
     * @param url The URL to which the request is sent
     * @param body The JSON payload to be sent with the request
     */
    public static void executePostRequest(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        System.out.println("Sending POST request to " + url + " with body " + body); // Print request details

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("POST Response Status: " + response.statusCode());
            MainFrame.displayHttpResponse(response.statusCode(), response.body(), overlay);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes an HTTP DELETE request to the specified URL.
     * @param url The URL from which to send the DELETE request
     */
    public static void executeDeleteRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        System.out.println("Sending DELETE request to " + url); // Print request details

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("DELETE Response Status: " + response.statusCode());
            MainFrame.displayHttpResponse(response.statusCode(), response.body(), overlay);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes an HTTP PUT request to the specified URL with the given JSON payload.
     * @param url The URL to which the request is sent
     * @param body The JSON payload to be sent with the request
     */
    public static void executePutRequest(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();

        System.out.println("Sending PUT request to " + url + " with body " + body); // Print request details

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("PUT Response Status: " + response.statusCode());
            MainFrame.displayHttpResponse(response.statusCode(), response.body(), overlay);
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Executes an HTTP GET request to the specified URL and returns the response as a string.
     * @param url The URL from which to retrieve the response
     * @return The response received from the server as a string
     */
    public static String executeGetRequest(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println("Sending GET request to " + url);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response from server: " + response.body());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
