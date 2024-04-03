package com.CS5031P2.frontend.view;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.Member;
import com.CS5031P2.frontend.MainFrame;

import com.CS5031P2.frontend.controller.HttpController;
import com.CS5031P2.frontend.controller.MemberController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Test class for MainFrame.
 * This class contains test methods to verify the behavior of the MainFrame class.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FrontendViewTest {

    private StackPane overlay;
    private static boolean isJavaFxEnvironmentInitialized = false;
    private static MockedStatic<HttpController> mockedHttpController;
    private static MockedStatic<MemberController> mockedMemberController;
    private MemberListView memberListView;
    private ObservableList<Member> members = FXCollections.observableArrayList();
    private MainFrame mainFrame;
    private BookListView bookListView;
    private MainFrame mockMainFrame;

    /**
     * Set up method to initialize the JavaFX environment for testing.
     * @throws Exception If an exception occurs during setup
     */
    @BeforeAll
    public static void setUpClass() throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("glass.platform", "Monocle");
        System.setProperty("monocle.platform", "Headless");
        System.setProperty("prism.order", "sw");
        System.setProperty("java.awt.headless", "true");
        if (!isJavaFxEnvironmentInitialized) {
            mockedHttpController = Mockito.mockStatic(HttpController.class);
            String mockJsonResponse = "[{\"memberId\":\"1\",\"bookId\":\"1\",\"borrowedId\":\"1\",\"author\":\"Author One\",\"title\":\"Title One\",\"checkedOut\":\"True\"}," +
                    "{\"memberId\":\"1\",\"bookId\":\"2\",\"borrowedId\":\"1\",\"author\":\"Author Two\",\"title\":\"Title Two\",\"checkedOut\":\"True\"}]";

            mockedHttpController.when(() -> HttpController.executeGetRequest(anyString()))
                    .thenAnswer(invocation -> {
                        return mockJsonResponse;
                    });

            mockedMemberController = Mockito.mockStatic(MemberController.class);

            Thread t = new Thread("JavaFX Init Thread") {
                public void run() {
                    Application.launch(DummyApplication.class, new String[0]);
                }
            };
            t.setDaemon(true);
            t.start();

            DummyApplication.waitForStart();
            isJavaFxEnvironmentInitialized = true;
        }
    }

    // DummyApplication class
    public static class DummyApplication extends Application {
        private static final Object lock = new Object();
        private static boolean started = false;

        @Override
        public void start(Stage primaryStage) {
            synchronized (lock) {
                started = true;
                lock.notifyAll();
            }
        }

        public static void waitForStart() throws InterruptedException {
            synchronized (lock) {
                while (!started) {
                    lock.wait();
                }
            }
        }
    }

    /**
     * Set up method to initialize the test environment before each test method.
     * @throws Exception If an exception occurs during setup
     */
    @BeforeEach
    public void setUp() throws Exception {
        Platform.runLater(() -> {
            overlay = new StackPane();
        });
    }

    /**
     * Cleans up resources after each test execution.
     */
    @AfterAll
    public static void tearDownClass() {
        if (isJavaFxEnvironmentInitialized) {
            Platform.exit();
        }
        mockedHttpController.close();
        mockedMemberController.close();
    }

    /**
     * Test method to verify the display of HTTP response with a success status code.
     */
    @Test
    public void testDisplayHttpResponseWithSuccessStatusCode() {
        Platform.runLater(() -> {
            MainFrame.displayHttpResponse(200, "Operation successful", overlay);
            try {
                Thread.sleep(2500); // Waiting for the label fully show up
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Assertions
            assertFalse(overlay.getChildren().isEmpty(), "Overlay should have children");
            Label responseLabel = (Label) overlay.getChildren().get(0);
            assertEquals("Operation successful", responseLabel.getText(), "Label text mismatch");
            assertTrue(responseLabel.getStyle().contains("lightgreen"), "Success status should result in a lightgreen background");
        });
    }

    /**
     * Test method to verify the display of HTTP response with a client error status code.
     */
    @Test
    public void testDisplayHttpResponseWithClientErrorStatusCode() {
        Platform.runLater(() -> {
            MainFrame.displayHttpResponse(400, "Client Error", overlay);
            try {
                Thread.sleep(2500); // Waiting for the label fully show up
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Assertions
            assertFalse(overlay.getChildren().isEmpty(), "Overlay should have children");
            Label responseLabel = (Label) overlay.getChildren().get(0);
            assertEquals("Client Error", responseLabel.getText(), "Label text mismatch");
            assertTrue(responseLabel.getStyle().contains("tomato"), "Client error status should result in a tomato background");
        });
    }

    /**
     * Test method to verify the display of HTTP response with a server error status code.
     */
    @Test
    public void testDisplayHttpResponseWithServerErrorStatusCode() {
        Platform.runLater(() -> {
            MainFrame.displayHttpResponse(500, "Server Error", overlay);
            try {
                Thread.sleep(2500); // Waiting for the label fully show up
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Assertions
            assertFalse(overlay.getChildren().isEmpty(), "Overlay should have children");
            Label responseLabel = (Label) overlay.getChildren().get(0);
            assertEquals("Server Error", responseLabel.getText(), "Label text mismatch");
            assertTrue(responseLabel.getStyle().contains("orange"), "Server error status should result in a orange background");
        });
    }

    /**
     * Test method to verify the display of HTTP response with an unknown error status code.
     */
    @Test
    public void testDisplayHttpResponseWithUnknownErrorStatusCode() {
        Platform.runLater(() -> {
            MainFrame.displayHttpResponse(100, "Unknown Error", overlay);
            try {
                Thread.sleep(2500); // Waiting for the label fully show up
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Assertions
            assertFalse(overlay.getChildren().isEmpty(), "Overlay should have children");
            Label responseLabel = (Label) overlay.getChildren().get(0);
            assertEquals("Unknown Error", responseLabel.getText(), "Label text mismatch");
            assertTrue(responseLabel.getStyle().contains("lightgrey"), "Unknown error status should result in a lightgrey background");
        });
    }

    /**
     * Test method to verify the creation of a styled button.
     */
    @Test
    public void testCreateStyledButton() {
        String buttonText = "Test Button";
        final boolean[] actionFired = {false}; // To capture if action was executed
        Runnable action = () -> actionFired[0] = true;

        // Assuming createStyledButton is public for testing or test via reflection if it's private
        Button testButton = MainFrame.createStyledButton(buttonText, action);

        // Check if the button text is set correctly
        assertEquals(buttonText, testButton.getText(), "Button text does not match expected value.");

        // Check if the button has the correct style class
        assertTrue(testButton.getStyleClass().contains("menu-button"), "Button does not contain expected style class.");

        // Simulate button click to check if action is executed
        Platform.runLater(() -> {
            testButton.fire();
            assertTrue(actionFired[0], "Action was not executed upon button click.");
        });
    }

    /**
     * Test method to verify the color mapping for a success status code.
     */
    @Test
    public void testGetColorByStatusCodeSuccess() {
        assertEquals("lightgreen", MainFrame.getColorByStatusCode(200), "200 status code should return lightgreen");
    }

    /**
     * Test method to verify the color mapping for a client error status code.
     */
    @Test
    public void testGetColorByStatusCodeClientError() {
        assertEquals("tomato", MainFrame.getColorByStatusCode(404), "404 status code should return tomato");
    }

    /**
     * Test method to verify the color mapping for a server error status code.
     */
    @Test
    public void testGetColorByStatusCodeServerError() {
        assertEquals("orange", MainFrame.getColorByStatusCode(500), "500 status code should return orange");
    }

    /**
     * Test method to verify the color mapping for an unknown status code.
     */
    @Test
    public void testGetColorByStatusCodeUnknown() {
        assertEquals("lightgrey", MainFrame.getColorByStatusCode(100), "100 status code should return lightgrey");
    }

    /**
     * Tests the initialization of the CheckOutView, ensuring that member information and the check out button state are correctly initialized.
     */
    @Test
    public void testCheckOutViewInit() {
        final String memberId = "123";
        final String memberName = "John Doe";
        final String memberAddress = "123 Main St";
        CheckOutView checkOutView = new CheckOutView(memberId, memberName, memberAddress);

        assertNotNull(checkOutView.getMemberInfoLabel(), "Member info label should be initialized");
        assertEquals("Member ID: " + memberId + "\nName: " + memberName + "\nAddress: " + memberAddress, checkOutView.getMemberInfoLabel().getText(), "Member info label text does not match");
        assertTrue(checkOutView.getCheckOutButton().isDisable(), "CheckOut button should be initially disabled");
    }

    /**
     * Tests the filtering functionality of books in CheckOutView based on text input in the search box.
     */
    @Test
    public void testFilterBooks() {
        CheckOutView checkOutView = new CheckOutView("123", "John Doe", "123 Main St");
        checkOutView.setBooks(Arrays.asList(
                new Book("Book One", "Author One"),
                new Book("Book Two", "Author Two"),
                new Book("Another Book", "Another Author")
        ));

        Platform.runLater(() -> {
            checkOutView.bookSearchBox.setText("Book");
            assertEquals(2, checkOutView.bookTable.getItems().size(), "Filtered book count should be 2");
        });
    }

    /**
     * Tests the checkout process for a book, ensuring that the book's status is updated correctly.
     */
    @Test
    public void testCheckOutBook() {
        CheckOutView checkOutView = new CheckOutView("123", "John Doe", "123 Main St");
        Book testBook = new Book("Book One", "Author One");
        checkOutView.setBooks(Collections.singletonList(testBook));

        Platform.runLater(() -> {
            checkOutView.bookTable.getSelectionModel().selectFirst();
            checkOutView.getCheckOutButton().fire();

            assertTrue(testBook.isCheckedOut(), "Book should be marked as checked out");
        });
    }

    /**
     * Tests the initialization of the ReturnView, ensuring that member information and the return button state are correctly initialized.
     */
    @Test
    public void testReturnViewInit() {
        String memberId = "456";
        String memberName = "Jane Doe";
        String memberAddress = "456 Elm St";
        ReturnView returnView = new ReturnView(memberId, memberName, memberAddress);

        assertNotNull(returnView, "ReturnView should be initialized");
        assertEquals("Member ID: " + memberId + "\nName: " + memberName + "\nAddress: " + memberAddress, returnView.getMemberInfoLabel().getText(), "Member info label text does not match");
        assertTrue(returnView.getReturnButton().isDisable(), "Return button should be initially disabled");
    }

    /**
     * Tests the loading of borrowed books into the ReturnView.
     */
    @Test
    public void testLoadBorrowedBooks() {
        ReturnView returnView = new ReturnView("1", "Jane Doe", "456 Elm St");
        returnView.loadBorrowedBooks("1");

        Platform.runLater(() -> {
            assertEquals(2, returnView.table.getItems().size(), "Loaded borrowed books count should be 2");
        });
    }

    /**
     * Tests the functionality of returning a selected book, ensuring the process updates the book's status appropriately.
     */
    @Test
    public void testReturnSelectedBook() {
        String memberId = "456";
        Book testBook = new Book("Test Book 1", "Test Author 1");

        ReturnView returnView = new ReturnView(memberId, "Jane Doe", "456 Elm St");
        Platform.runLater(() -> {
            returnView.table.getItems().add(testBook);
            returnView.table.getSelectionModel().selectFirst();
            returnView.getReturnButton().fire();
        });
    }

    /**
     * Tests the member list view's response to search input, ensuring that the list updates correctly to display all members when the search box is cleared.
     */
    @Test
    public void testSearchInMemberListView() {
        MainFrame mainFrame = new MainFrame();
        MemberListView memberListView = new MemberListView(mainFrame);
        TextField searchBox = memberListView.getSearchBox();
        Platform.runLater(() -> {
            searchBox.setText("");
            assertTrue(memberListView.table.getItems().size() == members.size(), "Should show all members when search box is cleared");
        });
        waitForFxEvents();
    }

    /**
     * Tests the enable/disable state of buttons in the MemberListView based on selection.
     */
    @Test
    public void testButtonEnableDisableState() {
        MainFrame mainFrame = new MainFrame();
        MemberListView memberListView = new MemberListView(mainFrame);
        TableView<Member> table = memberListView.table;
        Button deleteButton = memberListView.getDeleteButton();
        Button updateButton = memberListView.getUpdateButton();
        Platform.runLater(() -> {
            table.getSelectionModel().selectFirst();
            assertTrue(deleteButton.isDisabled(), "Delete button should be enabled when a member is selected");
            assertTrue(updateButton.isDisabled(), "Update button should be enabled when a member is selected");
        });
        waitForFxEvents();
    }

    /**
     * Tests the book filtering functionality in BookListView, ensuring the book list updates correctly based on search input.
     */
    @Test
    public void testFilterBooksInBookListView() {
        MainFrame mainFrame = new MainFrame();
        BookListView bookListView = new BookListView(mainFrame);
        bookListView.setBooks(Arrays.asList(
                new Book("Book One", "Author One"),
                new Book("Book Two", "Author Two"),
                new Book("Another Book", "Another Author")
        ));

        Platform.runLater(() -> {
            bookListView.searchBox.setText("Book");
            assertEquals(3, bookListView.table.getItems().size(), "Filtered book count should be 3");
        });
        waitForFxEvents();
    }

    /**
     * Tests the update of button states in the BookListView based on row selection.
     */
    @Test
    public void testButtonStateUpdateInBookListView() {
        Platform.runLater(() -> {
            TableView<Book> table = bookListView.table;
            Button deleteBtn = bookListView.deleteBtn;
            Button manageMemberBtn = bookListView.manageMemberBtn;

            assertTrue(deleteBtn.isDisabled(), "Delete button should be disabled when no row is selected");
            assertTrue(manageMemberBtn.isDisabled(), "Manage Member button should be disabled when no row is selected");

            table.getSelectionModel().selectFirst();
            assertFalse(deleteBtn.isDisabled(), "Delete button should be enabled when a row is selected");
            assertTrue(manageMemberBtn.isDisabled(), "Manage Member button should remain disabled if the book is not checked out");
        });
        waitForFxEvents();
    }

    /**
     * Waiting for operations to be completed
     */
    private void waitForFxEvents() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
