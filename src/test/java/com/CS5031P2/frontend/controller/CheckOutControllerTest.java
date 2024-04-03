package com.CS5031P2.frontend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Unit tests for the CheckOutController class.
 */
public class CheckOutControllerTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private MockedStatic<HttpController> mockedHttpController;

    /**
     * Sets up the necessary configurations and mocks before each test method execution.
     */
    @BeforeEach
    public void setUp() {
        // Necessary for code coverage
        CheckOutController checkOutController = new CheckOutController();

        System.setOut(new PrintStream(outputStreamCaptor));
        mockedHttpController = Mockito.mockStatic(HttpController.class);

        // Mock HTTP controller's methods
        mockedHttpController.when(() -> HttpController.executePostRequest(anyString(), anyString()))
                .thenAnswer(invocation -> {
                    System.out.println("Sending POST request to " + invocation.getArgument(0) + " with body " + invocation.getArgument(1));
                    return null;
                });
    }

    /**
     * Cleans up resources after each test method execution.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        mockedHttpController.close();
    }

    /**
     * Tests the CheckOutController checkOutBook method.
     */
    @Test
    public void testCheckOutBook() {
        String memberId = "1";
        String bookId = "1";
        CheckOutController.checkOutBook(memberId, bookId);
        String expectedOutput = "Sending POST request to http://localhost:8080/checkout/1/1 with body {}";
        String actualOutput = outputStreamCaptor.toString().trim();
        assertEquals(expectedOutput, actualOutput);
    }
}
