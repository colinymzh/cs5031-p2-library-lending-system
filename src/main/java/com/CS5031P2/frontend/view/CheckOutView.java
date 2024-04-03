package com.CS5031P2.frontend.view;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.frontend.controller.BookController;
import com.CS5031P2.frontend.controller.CheckOutController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.util.List;

/**
 * A view for checking out books by a member.
 * This view provides functionality to search for books, display them in a table,
 * and enable checking out a selected book by a member.
 */
public class CheckOutView extends VBox {
    private final String memberId;
    TableView<Book> bookTable;
    private final ObservableList<Book> bookData = FXCollections.observableArrayList();
    private Button checkOutButton;
    private final Label memberInfoLabel;
    private final Label checkOutTitle;
    TextField bookSearchBox;

    /**
     * Constructs a new CheckOutView with the specified member information.
     *
     * @param memberId      The ID of the member.
     * @param memberName    The name of the member.
     * @param memberAddress The address of the member.
     */
    public CheckOutView(String memberId, String memberName, String memberAddress) {
        // Set member information
        checkOutTitle = new Label("Check Out");
        checkOutTitle.setStyle("-fx-font-size: 30px");
        Label memberInfoLabelTitle = new Label("Current Member:");
        memberInfoLabelTitle.setStyle("-fx-font-size: 18px");
        memberInfoLabel = new Label("Member ID: " + memberId + "\nName: " + memberName + "\nAddress: " + memberAddress);
        memberInfoLabel.setPadding(new Insets(5));
        memberInfoLabel.setStyle("-fx-font-size: 16px;");
        this.memberId = memberId;

        initializeView();
    }

    /**
     * Initializes the UI components and sets up event handlers.
     */
    private void initializeView() {
        // Book search box
        bookSearchBox = new TextField();
        bookSearchBox.setPromptText("Search Book...");
        setupSearchBox(bookSearchBox);

        bookTable = new TableView<>();
        setupTable();

        // CheckOut button
        checkOutButton = new Button("Check Out");
        checkOutButton.getStyleClass().add("add-button-style");
        checkOutButton.setOnAction(event -> checkOut());
        checkOutButton.setDisable(true); // Initially disabled

        // Listen for selections to enable checkOut button
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateCheckOutButtonState());

        VBox mainLayout = new VBox(10, checkOutTitle, new Separator(), memberInfoLabel, bookSearchBox, bookTable, checkOutButton);
        mainLayout.setPadding(new Insets(10));
        VBox.setVgrow(bookTable, Priority.ALWAYS);

        this.getChildren().addAll(mainLayout);
        Platform.runLater(bookSearchBox::requestFocus);
    }

    /**
     * Set up the search box for user to enter information about the book.
     *
     * @param searchBox A text field for user to enter text.
     */
    private void setupSearchBox(TextField searchBox) {
        searchBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));
    }

    /**
     * Set up the book information table.
     */
    private void setupTable() {
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        bookTable.getColumns().addAll(bookIdColumn, titleColumn, authorColumn);
        bookTable.setItems(bookData);

        // Custom row factory to disable selection of checked out books
        bookTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);
                if (book == null || empty) {
                    setStyle("");
                    setDisable(false);
                } else if (book.isCheckedOut()) {
                    setStyle("-fx-opacity: 0.5;"); // Make the row look disabled
                    setDisable(true); // Disable selection for this row
                } else {
                    setStyle("");
                    setDisable(false);
                }
            }
        });

        bookTable.refresh();

        bookTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Filters the books displayed in the table based on the given query.
     *
     * @param query The search query to filter books.
     */
    private void filterBooks(String query) {
        if (query.isEmpty()) {
            bookTable.setItems(bookData); // Show all books if search box is empty
        } else {
            ObservableList<Book> filteredData = FXCollections.observableArrayList();
            for (Book book : bookData) {
                // Check if query matches any field of the book (bookId, title, author)
                if (book.getBookId().toLowerCase().contains(query.toLowerCase()) ||
                        book.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                    filteredData.add(book);
                }
            }
            bookTable.setItems(filteredData); // Show filtered list
        }
    }

    /**
     * Updates the state of the check out button based on whether a book is selected.
     */
    private void updateCheckOutButtonState() {
        boolean bookSelected = !bookTable.getSelectionModel().isEmpty();
        checkOutButton.setDisable(!bookSelected);
    }

    /**
     * Handles the action of checking out a book.
     */
    private void checkOut() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            CheckOutController.checkOutBook(memberId, selectedBook.getBookId());
        }
        bookSearchBox.setText("");
        var books = BookController.getAllBooks();
        bookTable.getItems().setAll(books);
    }

    /**
     * Sets the list of books to be displayed in the table.
     *
     * @param books The list of books to display.
     */
    public void setBooks(List<Book> books) {
        bookData.setAll(books);
    }

    /**
     * Method to return the member info label
     *
     * @return Member info label
     */
    public Label getMemberInfoLabel() {
        return this.memberInfoLabel;
    }

    /**
     * Method to return the Checkout button
     *
     * @return Checkout button
     */
    public Button getCheckOutButton() {
        return this.checkOutButton;
    }
}

