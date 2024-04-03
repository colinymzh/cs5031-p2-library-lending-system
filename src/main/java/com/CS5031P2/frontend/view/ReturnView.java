package com.CS5031P2.frontend.view;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.frontend.controller.ReturnController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * A JavaFX view component for returning borrowed books.
 */
public class ReturnView extends VBox {

    TableView<Book> table;
    private final ObservableList<Book> borrowedBookData = FXCollections.observableArrayList();
    private final String memberId;
    private final Label memberInfoLabel;
    private final Label returnTitle;
    private final Label memberInfoLabelTitle;
    private Button returnButton;

    /**
     * Constructs a new ReturnView instance.
     *
     * @param memberId      The ID of the member returning the books.
     * @param memberName    The name of the member returning the books.
     * @param memberAddress The address of the member returning the books.
     */
    public ReturnView(String memberId, String memberName, String memberAddress) {
        this.memberId = memberId;

        // Creating and setting up member information labels
        returnTitle = new Label("Return");
        returnTitle.setStyle("-fx-font-size: 30px");
        memberInfoLabelTitle = new Label("Current Member:");
        memberInfoLabelTitle.setStyle("-fx-font-size: 18px");
        String memberInfoText = String.format("Member ID: %s\nName: %s\nAddress: %s", memberId, memberName, memberAddress);
        memberInfoLabel = new Label(memberInfoText);
        memberInfoLabel.setPadding(new Insets(5));
        memberInfoLabel.setStyle("-fx-font-size: 16px;");

        initializeView();
        loadBorrowedBooks(memberId);
    }

    /**
     * Initializes the view components.
     */
    private void initializeView() {
        // Initialize table
        table = new TableView<>();
        initializeTable();

        returnButton = new Button("Return Selected Book");
        returnButton.getStyleClass().add("add-button-style");
        returnButton.setOnAction(event -> returnSelectedBook());
        returnButton.setDisable(true); // Disabled upon setting up

        // Update the availability of the return button when the selection in the form changes
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> returnButton.setDisable(newSelection == null));

        // Layout setting
        VBox mainLayout = new VBox(10, returnTitle, new Separator(), memberInfoLabelTitle, memberInfoLabel, table, returnButton);
        mainLayout.setPadding(new Insets(10));

        VBox.setVgrow(table, Priority.ALWAYS);

        this.getChildren().addAll(mainLayout);
        Platform.runLater(table::requestFocus);
    }

    /**
     * Initializes the table columns and settings.
     */
    private void initializeTable() {
        TableColumn<Book, String> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(bookIdColumn, titleColumn, authorColumn);
        table.setItems(borrowedBookData);
    }

    /**
     * Loads borrowed books for the given member ID.
     *
     * @param memberId The ID of the member.
     */
    void loadBorrowedBooks(String memberId) {
        borrowedBookData.clear();
        if (!memberId.trim().isEmpty()) {
            borrowedBookData.addAll(ReturnController.getBorrowedBooksByMember(memberId));
        }
    }

    /**
     * Handles returning the selected book.
     */
    private void returnSelectedBook() {
        Book selectedBook = table.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            ReturnController.returnBook(memberId, selectedBook.getBookId());
            loadBorrowedBooks(memberId);
        }
    }

    /**
     * Method to return the return book button
     *
     * @return Return book button
     */
    public Button getReturnButton() {
        return this.returnButton;
    }

    /**
     * Method to return the member info label
     *
     * @return Member info label
     */
    public Label getMemberInfoLabel() {
        return this.memberInfoLabel;
    }
}

