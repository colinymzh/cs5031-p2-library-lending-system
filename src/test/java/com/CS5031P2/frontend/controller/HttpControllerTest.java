package com.CS5031P2.frontend.controller;

import com.CS5031P2.frontend.MainFrame;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the HttpController class.
 */
class HttpControllerTest {
    private HttpClient mockHttpClient;
    private HttpResponse<String> mockHttpResponse;

    /**
     * Sets up the necessary configurations and mocks before each test method execution.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @BeforeEach
    void setUp() throws IOException, InterruptedException {
        // Only necessary for test coverage
        HttpController controller = new HttpController();

        // Mock HttpClient and HttpResponse
        mockHttpClient = Mockito.mock(HttpClient.class);
        mockHttpResponse = Mockito.mock(HttpResponse.class);

        // Inject mock HttpClient into HttpController
        HttpController.setHttpClient(mockHttpClient);
    }

    /**
     * Cleans up resources after each test method execution.
     */
    @AfterEach
    void tearDown() {
        // Reset HttpController to use the default HttpClient after tests
        HttpController.setHttpClient(HttpClient.newHttpClient());
    }

    /**
     * Tests the executeGetRequest method.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecuteGetRequest() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/test";
        String expectedResponseBody = "response body";

        // Setup mock behavior
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(expectedResponseBody);

        // Execute the method under test
        String actualResponse = HttpController.executeGetRequest(testUrl);

        // Assert the response is as expected
        assertEquals(expectedResponseBody, actualResponse);
    }

    /**
     * Tests the executePostRequest method.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecutePostRequest() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testPost";
        String requestBody = "{\"key\": \"value\"}";

        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);

        try (MockedStatic<MainFrame> mockedMainFrame = Mockito.mockStatic(MainFrame.class)) {
            mockedMainFrame.when(() -> MainFrame.displayHttpResponse(anyInt(), anyString(), any())).thenAnswer(invocation -> null);

            // Execute the method under test
            HttpController.executePostRequest(testUrl, requestBody);

            // Verify HttpClient call
            verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }
    }

    /**
     * Tests the executeDeleteRequest method.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecuteDeleteRequest() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testDelete";

        // Setup mock behavior
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);

        try (MockedStatic<MainFrame> mockedMainFrame = Mockito.mockStatic(MainFrame.class)) {
            mockedMainFrame.when(() -> MainFrame.displayHttpResponse(anyInt(), anyString(), any())).thenAnswer(invocation -> null);

            // Execute the method under test
            HttpController.executeDeleteRequest(testUrl);

            // Verify HttpClient call
            verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }
    }

    /**
     * Tests the executePutRequest method.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecutePutRequest() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testPut";
        String requestBody = "{\"key\": \"updatedValue\"}";

        // Setup mock behavior
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockHttpResponse);

        try (MockedStatic<MainFrame> mockedMainFrame = Mockito.mockStatic(MainFrame.class)) {
            mockedMainFrame.when(() -> MainFrame.displayHttpResponse(anyInt(), anyString(), any())).thenAnswer(invocation -> null);

            // Execute the method under test
            HttpController.executePutRequest(testUrl, requestBody);

            // Verify HttpClient call
            verify(mockHttpClient).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        }
    }

    /**
     * Tests the executeGetRequest method when an exception is thrown.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecuteGetRequestException() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testGet";

        when(mockHttpClient.send(any(), any())).thenThrow(IOException.class);

        HttpController.executeGetRequest(testUrl);

        verify(mockHttpClient).send(any(), any());
    }

    /**
     * Tests the executePostRequest method when an exception is thrown.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecutePostRequestException() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testPost";
        String requestBody = "{\"key\": \"value\"}";

        when(mockHttpClient.send(any(), any())).thenThrow(IOException.class);

        HttpController.executePostRequest(testUrl, requestBody);

        verify(mockHttpClient).send(any(), any());
    }

    /**
     * Tests the executeDeleteRequest method when an exception is thrown.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecuteDeleteRequestException() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testDelete";

        when(mockHttpClient.send(any(), any())).thenThrow(IOException.class);

        HttpController.executeDeleteRequest(testUrl);

        verify(mockHttpClient).send(any(), any());
    }

    /**
     * Tests the executePutRequest method when an exception is thrown.
     *
     * @throws IOException        if an I/O error occurs
     * @throws InterruptedException if the operation is interrupted
     */
    @Test
    void testExecutePutRequestException() throws IOException, InterruptedException {
        String testUrl = "http://localhost:8080/testPut";
        String requestBody = "{\"key\": \"updatedValue\"}";

        when(mockHttpClient.send(any(), any())).thenThrow(IOException.class);

        HttpController.executePutRequest(testUrl, requestBody);

        verify(mockHttpClient).send(any(), any());
    }
}
