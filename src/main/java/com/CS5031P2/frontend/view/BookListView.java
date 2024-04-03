package com.CS5031P2.frontend.view;

import com.CS5031P2.backend.model.Book;
import com.CS5031P2.frontend.MainFrame;
import com.CS5031P2.frontend.controller.BookController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.Objects;

/**
 * A JavaFX view component for displaying a list of books with Add Delete operations.
 */
public class BookListView extends VBox {
    TableView<Book> table;
    private final ObservableList<Book> bookData = FXCollections.observableArrayList();
    TextField searchBox;
    Button deleteBtn;
    Button manageMemberBtn;
    private final MainFrame mainFrame;

    /**
     * Constructs a new instance of BookListView.
     */
    public BookListView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeView();
    }

    /**
     * Initializes the UI components and sets up event handlers.
     */
    private void initializeView() {
        Label bookListViewTitle = new Label("Book List");
        bookListViewTitle.setStyle("-fx-font-size: 30px");
        // Initialize search box
        searchBox = new TextField();
        searchBox.setPromptText("Search...");
        searchBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filterBooks(newValue));

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Book, String> bookIdColumn = new TableColumn<>("Book ID");
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, Boolean> checkedOutColumn = new TableColumn<>("Checked Out");
        checkedOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkedOut"));

        TableColumn<Book, Boolean> borrowedIdColumn = new TableColumn<>("Borrowed By");
        borrowedIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowedId"));

        table.getColumns().addAll(bookIdColumn, titleColumn, authorColumn, checkedOutColumn, borrowedIdColumn);
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Bottom buttons
        Button refreshBtn = new Button("Refresh");
        deleteBtn = new Button("Delete");
        Button addBtn = new Button("Add");
        manageMemberBtn = new Button("Manage Member");

        // Apply style css
        refreshBtn.getStyleClass().add("refresh-button-style");
        deleteBtn.getStyleClass().add("delete-button-style");
        addBtn.getStyleClass().add("add-button-style");
        manageMemberBtn.getStyleClass().add("refresh-button-style");

        // Set event handling for button click
        refreshBtn.setOnAction(event -> refreshBooks());
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        deleteBtn.setOnAction(event -> deleteSelectedBook());
        deleteBtn.setDisable(true); // Initially disabled
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        addBtn.setOnAction(event -> addBook());

        manageMemberBtn.setOnAction(event -> {
            Book selectedBook = table.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                mainFrame.showMemberListWithSearch(selectedBook.getBorrowedId());
            }
        });

        // Buttons container
        HBox buttonBar = new HBox(10, refreshBtn, deleteBtn, addBtn, manageMemberBtn);

        // Main layout setting
        VBox mainLayout = new VBox(10, bookListViewTitle, new Separator(),searchBox, table, buttonBar);
        mainLayout.setPadding(new Insets(10));
        VBox.setVgrow(table, Priority.ALWAYS);

        this.getChildren().addAll(mainLayout);
        Platform.runLater(searchBox::requestFocus);

        refreshBooks(); // Initialize book data
        String css = Objects.requireNonNull(this.getClass().getResource("/style.css")).toExternalForm();
        this.getStylesheets().add(css);
    }

    /**
     * Sets the list of books to be displayed.
     *
     * @param books The list of books to display.
     */
    public void setBooks(List<Book> books) {
        bookData.setAll(books);
    }

    /**
     * Refreshes the list of books displayed.
     */
    void refreshBooks() {
        searchBox.setText("");
        // Call Controller to get all books and refresh the table
        var books = BookController.getAllBooks();
        table.getItems().setAll(books);
    }

    /**
     * Opens a dialog for adding a new book.
     */
    private void addBook() {
        // Set up dialog
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle("Add Book");
        dialog.setHeaderText("Enter Book details:");

        // Set button type
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Set text field
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        titleField.setPrefColumnCount(30);

        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        authorField.setPrefColumnCount(30);

        // Add fields to grid
        grid.add(new Label("Title:"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Author:"), 0, 2);
        grid.add(authorField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Request focus on ID input box
        Platform.runLater(titleField::requestFocus);

        // Lookup the Add button
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true); // Initially disable the Add button to enforce validation

        // Validation logic for enabling the Add button
        Runnable validateInputs = () -> {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            boolean isInputValid = !title.isEmpty() && !author.isEmpty();
            addButton.setDisable(!isInputValid); // Enable the Add button only if all inputs are valid
        };

        // Add text listeners to validate on input change
        titleField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());
        authorField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Book(titleField.getText(), authorField.getText());
            }
            return null;
        });

        Optional<Book> result = dialog.showAndWait();
        result.ifPresent(book -> {
            // Call Controller to add member
            BookController.addBook(book.getTitle(), book.getAuthor());
            refreshBooks(); // Refresh table after adding
        });
    }

    /**
     * Deletes the selected book from the table.
     */
    private void deleteSelectedBook() {
        for (Book selectedBook : table.getSelectionModel().getSelectedItems()) {
            BookController.removeBook(selectedBook.getBookId());
        }
        refreshBooks(); // Refresh table after deleting
    }

    /**
     * Filters the books displayed based on the given query.
     *
     * @param query The search query.
     */
    private void filterBooks(String query) {
        if (query.isEmpty()) {
            table.setItems(bookData); // Show all books if search box is empty
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
            table.setItems(filteredData); // Show filtered list
        }
    }

    /**
     * Updates the state of the delete buttons based on the selection in the table.
     */
    private void updateButtonState() {
        // Check if a row is selected
        boolean isRowSelected = !table.getSelectionModel().isEmpty();
        if (isRowSelected) {
            // Get the selected book
            Book selectedBook = table.getSelectionModel().getSelectedItem();
            // Check if the selected book is checked out
            boolean isBookCheckedOut = selectedBook.isCheckedOut();
            // Disable buttons if the book is checked out, enable them otherwise
            deleteBtn.setDisable(isBookCheckedOut);
            manageMemberBtn.setDisable(!isBookCheckedOut);
        } else {
            // Disable buttons if no row is selected
            deleteBtn.setDisable(true);
            manageMemberBtn.setDisable(true);
        }
    }

}

