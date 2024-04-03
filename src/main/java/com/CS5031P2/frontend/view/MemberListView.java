package com.CS5031P2.frontend.view;

import com.CS5031P2.backend.model.Member;
import com.CS5031P2.frontend.MainFrame;
import com.CS5031P2.frontend.controller.MemberController;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import java.util.Objects;
import java.util.Optional;

/**
 * A JavaFX view component for displaying a list of members with CRUD operations.
 */
public class MemberListView extends VBox {
    TableView<Member> table;
    private final ObservableList<Member> memberData = FXCollections.observableArrayList();
    private final MainFrame mainFrame;
    private Button updateBtn, deleteBtn, checkOutBtn, returnBtn;
    private TextField searchBox;

    /**
     * Constructs a new instance of MemberListView.
     *
     * @param mainFrame The MainFrame instance associated with this view.
     */
    public MemberListView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeView();
    }

    /**
     * Initializes the view components including the table and buttons.
     */
    private void initializeView() {
        Label memberListViewTitle = new Label("Member List");
        memberListViewTitle.setStyle("-fx-font-size: 30px");
        // Initialize search box
        searchBox = new TextField();
        searchBox.setPromptText("Search...");
        searchBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchBox, Priority.ALWAYS);
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> filterMembers(newValue));

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Member, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));

        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Member, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Member, Number> borrowedBooksCountColumn = new TableColumn<>("Borrowed Books Count");
        borrowedBooksCountColumn.setCellValueFactory(cellData -> {
            Member member = cellData.getValue();
            int borrowedBooksCount = member.getBorrowedBooks().size();
            return new ReadOnlyObjectWrapper<>(borrowedBooksCount);
        });


        table.getColumns().addAll(idColumn, nameColumn, addressColumn, borrowedBooksCountColumn);

        // Disable multiple selection
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Bottom button
        Button refreshBtn = new Button("Refresh");
        updateBtn = new Button("Update");
        deleteBtn = new Button("Delete");
        Button addBtn = new Button("Add");
        checkOutBtn = new Button("Check Out");
        returnBtn = new Button("Return");

        // Apply style css
        refreshBtn.getStyleClass().add("refresh-button-style");
        updateBtn.getStyleClass().add("update-button-style");
        deleteBtn.getStyleClass().add("delete-button-style");
        addBtn.getStyleClass().add("add-button-style");
        checkOutBtn.getStyleClass().add("add-button-style");
        returnBtn.getStyleClass().add("add-button-style");

        // Set event handling for button click
        refreshBtn.setOnAction(event -> refreshMembers());

        updateBtn.setOnAction(event -> updateMember());
        updateBtn.setDisable(true); // Initially disabled
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        deleteBtn.setOnAction(event -> deleteSelectedMembers());
        deleteBtn.setDisable(true); // Initially disabled
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        addBtn.setOnAction(event -> addMember());
        returnBtn.setOnAction(event -> {
            Member selectedMember = table.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                mainFrame.showReturn(selectedMember.getMemberId(), selectedMember.getName(), selectedMember.getAddress());
            }
        });
        returnBtn.setDisable(true); // Initially disabled
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        checkOutBtn.setOnAction(event -> {
            Member selectedMember = table.getSelectionModel().getSelectedItem();
            if (selectedMember != null) {
                mainFrame.showCheckOut(selectedMember.getMemberId(), selectedMember.getName(), selectedMember.getAddress());
            }
        });
        checkOutBtn.setDisable(true); // Initially disabled
        // Listen for selections to enable checkOut button
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> updateButtonState());

        // Adjust layout to include search box
        HBox searchBoxContainer = new HBox(searchBox);
        searchBoxContainer.setPadding(new Insets(10, 0, 10, 0)); // Add some padding around the search box

        HBox buttonBar = new HBox(10, refreshBtn, updateBtn, deleteBtn, addBtn, checkOutBtn, returnBtn);
        VBox mainLayout = new VBox(10, memberListViewTitle, new Separator(), searchBox, table, buttonBar);
        VBox.setVgrow(table, Priority.ALWAYS);
        mainLayout.setPadding(new Insets(10));

        this.getChildren().addAll(mainLayout);
        Platform.runLater(searchBox::requestFocus);

        refreshMembers(); // Initialize member data
        String css = Objects.requireNonNull(this.getClass().getResource("/style.css")).toExternalForm();
        this.getStylesheets().add(css);
    }

    /**
     * Sets the list of members to be displayed in the table.
     * @param members The list of members to be displayed
     */
    public void setMembers(List<Member> members) {
        memberData.setAll(members);
    }

    /**
     * Refreshes the table by retrieving the latest member data from the controller.
     */
    void refreshMembers() {
        searchBox.setText("");
        // Call Controller to get all members and refresh the table
        var members = MemberController.getAllMembers();
        table.getItems().setAll(members);
    }

    /**
     * Opens a dialog for adding a new member.
     */
    private void addMember() {
        // Set up dialog
        Dialog<Member> dialog = new Dialog<>();
        dialog.setTitle("Add Member");
        dialog.setHeaderText("Enter member details:");

        // Set button type
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Set text field
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        nameField.setPrefColumnCount(30);

        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        addressField.setPrefColumnCount(30);

        // Add fields to grid
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Address:"), 0, 2);
        grid.add(addressField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Request focus on ID input box
        Platform.runLater(nameField::requestFocus);

        // Lookup the Add button
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true); // Initially disable the Add button to enforce validation

        // Validation logic for enabling the Add button
        Runnable validateInputs = () -> {
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();
            boolean isInputValid = !name.isEmpty() && !address.isEmpty();
            addButton.setDisable(!isInputValid); // Enable the Add button only if all inputs are valid
        };

        // Add text listeners to validate on input change
        nameField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());
        addressField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Member(nameField.getText(), addressField.getText());
            }
            return null;
        });

        Optional<Member> result = dialog.showAndWait();

        result.ifPresent(member -> {
            // Call Controller to add member
            MemberController.addMember(member.getName(), member.getAddress());
            refreshMembers(); // Refresh table after adding
        });
    }

    /**
     * Deletes the selected members from the table.
     */
    private void deleteSelectedMembers() {
        for (Member selectedMember : table.getSelectionModel().getSelectedItems()) {
            MemberController.removeMember(selectedMember.getMemberId());
        }
        refreshMembers(); // Refresh table after deleting
    }

    /**
     * Opens a dialog for updating the details of a selected member.
     */
    private void updateMember() {
        Member selectedMember = table.getSelectionModel().getSelectedItem();
        if (selectedMember != null) {
            Dialog<Member> dialog = new Dialog<>();
            dialog.setTitle("Update Member");
            dialog.setHeaderText("Update Name and Address:");

            // Set button type
            ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField nameField = new TextField(selectedMember.getName());
            nameField.setPrefColumnCount(30);

            TextField addressField = new TextField(selectedMember.getAddress());
            addressField.setPrefColumnCount(30);

            grid.add(new Label("Name:"), 0, 0);
            grid.add(nameField, 1, 0);
            grid.add(new Label("Address:"), 0, 1);
            grid.add(addressField, 1, 1);

            dialog.getDialogPane().setContent(grid);

            // Disable the Update button initially and enforce validation
            Node updateButton = dialog.getDialogPane().lookupButton(updateButtonType);
            updateButton.setDisable(true);

            // Validation logic to enable the Update button
            Runnable validateInputs = () -> {
                String newName = nameField.getText().trim();
                String newAddress = addressField.getText().trim();
                boolean isInputValid = !newName.isEmpty() && !newAddress.isEmpty();
                updateButton.setDisable(!isInputValid);
            };

            // Add text listeners to validate on input change
            nameField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());
            addressField.textProperty().addListener((observable, oldValue, newValue) -> validateInputs.run());

            // Perform initial validation
            validateInputs.run();

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == updateButtonType && !updateButton.isDisabled()) {
                    // Input validation passed, update the member
                    MemberController.updateMember(selectedMember.getMemberId(), nameField.getText(), addressField.getText());
                    return new Member(nameField.getText(), addressField.getText());
                }
                return null;
            });

            dialog.showAndWait().ifPresent(member -> refreshMembers()); // Refresh table after updating
        }
    }

    /**
     * Filters the member list based on a search query across all fields.
     * @param query The search query.
     */
    private void filterMembers(String query) {
        if (query.isEmpty()) {
            table.setItems(memberData); // Show all members if search box is empty
        } else {
            ObservableList<Member> filteredData = FXCollections.observableArrayList();
            for (Member member : memberData) {
                // Check if query matches any field of the member (ID, name, address)
                if (member.getMemberId().toLowerCase().contains(query.toLowerCase()) ||
                        member.getName().toLowerCase().contains(query.toLowerCase()) ||
                        member.getAddress().toLowerCase().contains(query.toLowerCase())) {
                    filteredData.add(member);
                }
            }
            table.setItems(filteredData); // Show filtered list
        }
    }

    /**
     * Updates the state of the buttons based on whether a member is selected and the quantity
     * of borrowed books.
     */
    private void updateButtonState() {
        // Get the selected member from the table selection model
        Member selectedMember = table.getSelectionModel().getSelectedItem();

        // Check if a member has been selected
        if (selectedMember != null) {
            // Determine the disable state based on the borrowed books count
            boolean disableButtons = selectedMember.getBorrowedBooks().size() != 0;

            // Apply the determined disable state to the Update and Delete buttons
            updateBtn.setDisable(false);
            deleteBtn.setDisable(disableButtons);

            checkOutBtn.setDisable(false);
            returnBtn.setDisable(selectedMember.getBorrowedBooks().size() == 0);
        } else {
            // No member selected, disable all buttons
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            checkOutBtn.setDisable(true);
            returnBtn.setDisable(true);
        }
    }

    /**
     * A method to pre-fill member id.
     */
    public void searchMemberById(String memberId) {
        if (memberId != null && !memberId.trim().isEmpty()) {
            // Set the search box text and trigger the search
            searchBox.setText(memberId);
            filterMembers(memberId);
        }
    }

    /**
     * Return the search box
     *
     * @return Search box text field
     */
    public TextField getSearchBox() {
        return this.searchBox;
    }

    /**
     * Return the delete button
     *
     * @return Delete button
     */
    public Button getDeleteButton() {
        return this.deleteBtn;
    }

    /**
     * Return the update button
     *
     * @return Update button
     */
    public Button getUpdateButton() {
        return this.updateBtn;
    }
}

