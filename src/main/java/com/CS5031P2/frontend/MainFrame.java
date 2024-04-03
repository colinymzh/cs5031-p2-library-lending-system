package com.CS5031P2.frontend;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.backend.model.Member;
import com.CS5031P2.frontend.controller.BookController;
import com.CS5031P2.frontend.controller.MemberController;
import com.CS5031P2.frontend.view.BookListView;
import com.CS5031P2.frontend.view.CheckOutView;
import com.CS5031P2.frontend.view.MemberListView;
import com.CS5031P2.frontend.view.ReturnView;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import java.util.Objects;

/**
 * The main frame of the Library Management System.
 * This class extends JavaFX's Application class and serves as the entry point of the frontend.
 */
public class MainFrame extends Application {
    private static BorderPane root; // Class member variable for the root pane
    public static StackPane overlay; // Class member variable for the overlay pane to display http response

    /**
     * Start method called when the application is launched.
     * @param primaryStage The primary stage of the application
     */
    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();
        overlay = new StackPane(); // Containers for hovering elements
        overlay.setPickOnBounds(false); // Allow click through to the following components

        StackPane mainLayout = new StackPane();
        mainLayout.getChildren().add(root);
        mainLayout.getChildren().add(overlay); // Finally add overlay to make sure it's on the top layer

        // Create the VBox for the menu
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(15));
        menuBox.setPrefWidth(200); // Set the width of the VBox
        menuBox.getStyleClass().add("menu-box");

        // Create the book area
        Label bookLabel = new Label("Book");
        bookLabel.getStyleClass().add("section-label");
        VBox bookSection = new VBox(5);
        bookSection.getChildren().addAll(
                createStyledButton("Manage Book", this::showBookList)
        );

        // Create the member area
        Label memberLabel = new Label("Member");
        memberLabel.getStyleClass().add("section-label");
        VBox memberSection = new VBox(5);
        memberSection.getChildren().addAll(
                createStyledButton("Manage Member", this::showMemberList)
        );

        // Add the labels and sections to the menuBox
        menuBox.getChildren().addAll(bookLabel, bookSection, new Separator(), memberLabel, memberSection, new Separator());

        // Set the menuBox to the left side of the main layout
        root.setLeft(menuBox);

        // Create the scene and add a stylesheet css
        Scene scene = new Scene(mainLayout, 1200, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System");
        primaryStage.show();
    }

    /**
     * Create a styled button with the given text and action.
     * @param text The text to be displayed on the button
     * @param action The action to be performed when the button is clicked
     * @return The styled button
     */
    public static Button createStyledButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE); // Make the button width fill the container
        button.getStyleClass().add("menu-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    /**
     * Displays the member list view in the center of the main layout.
     */
    private void showMemberList() {
        MemberListView memberListView = new MemberListView(this);
        List<Member> members = MemberController.getAllMembers();
        memberListView.setMembers(members);
        root.setCenter(memberListView);
    }

    /**
     * Displays the book list view in the center of the main layout.
     */
    private void showBookList() {
        BookListView bookListView = new BookListView(this);
        List<Book> books = BookController.getAllBooks();
        bookListView.setBooks(books);
        root.setCenter(bookListView);
    }

    /**
     * Displays the checkout view in the center of the main layout.
     */
    public void showCheckOut(String memberId, String name, String address) {
        CheckOutView checkOutView = new CheckOutView(memberId, name, address);
        List<Book> books = BookController.getAllBooks();
        checkOutView.setBooks(books);
        root.setCenter(checkOutView);
    }

    /**
     * Displays the return view in the center of the main layout.
     */
    public void showReturn(String memberId, String name, String address) {
        ReturnView returnView = new ReturnView(memberId, name, address);
        root.setCenter(returnView);
    }

    /**
     * Displays the member list view in the center of the main layout with pre-filled member id.
     */
    public void showMemberListWithSearch(String memberId) {
        MemberListView memberListView = new MemberListView(this);
        List<Member> members = MemberController.getAllMembers();
        memberListView.setMembers(members);
        memberListView.searchMemberById(memberId);
        root.setCenter(memberListView);
    }

    /**
     * Displays the HTTP response in an overlay on the UI.
     * @param statusCode The status code of the HTTP response
     * @param content The content of the HTTP response
     * @param overlay The stack pane to display the HTTP response
     */
    public static void displayHttpResponse(int statusCode, String content, StackPane overlay) {
        Platform.runLater(() -> {
            Label responseLabel = new Label(content);
            responseLabel.setWrapText(true);
            responseLabel.setMaxWidth(199);
            responseLabel.setStyle("-fx-background-color: " + getColorByStatusCode(statusCode) + "; -fx-background-radius: 5; -fx-padding: 10; -fx-font-size: 16px;");

            StackPane.setAlignment(responseLabel, Pos.BOTTOM_LEFT);

            // Ensure that overlay does not block other UI operations
            overlay.getChildren().add(responseLabel);
            overlay.setMouseTransparent(true); // Allow mouse events to pass through overlay

            // Creating a fade-in effect
            FadeTransition fadeInTransition = new FadeTransition(Duration.seconds(1.5), responseLabel);
            fadeInTransition.setFromValue(0.0);
            fadeInTransition.setToValue(1.0);
            fadeInTransition.setOnFinished(event -> {
                // After the fade-in is complete, the fade-out effect begins
                FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(3), responseLabel);
                fadeOutTransition.setFromValue(1.0);
                fadeOutTransition.setToValue(0.0);
                fadeOutTransition.setDelay(Duration.seconds(3)); // Delay for 3 seconds to display label
                fadeOutTransition.setOnFinished(e -> overlay.getChildren().remove(responseLabel));
                fadeOutTransition.play();
            });
            fadeInTransition.play();
        });
    }

    /**
     * Get the color based on the HTTP status code.
     * @param statusCode The status code of the HTTP response
     * @return The color corresponding to the status code
     */
    public static String getColorByStatusCode(int statusCode) {
        if (statusCode >= 200 && statusCode < 300) {
            return "lightgreen"; // Success
        } else if (statusCode >= 400 && statusCode < 500) {
            return "tomato"; // Client Error
        } else if (statusCode >= 500) {
            return "orange"; // Server Error
        } else {
            return "lightgrey"; // Unknown Error
        }
    }

    /**
     * The entry point of the application.
     * @param args The command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}