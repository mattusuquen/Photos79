package src.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import src.DataManager;
import src.User;

/**
 * Controller class for the admin user management interface.
 * Handles creation and deletion of users, as well as logout and exit operations.
 */
public class AdminController {

    /** Table view displaying all users */
    @FXML private TableView<User> userTableView;

    /** Table column displaying usernames */
    @FXML private TableColumn<User, String> usernameColumn;

    /** Text field to enter a new username */
    @FXML private TextField newUsernameField;

    /** Label used to display status messages */
    @FXML private Label statusLabel;

    /** Internal list of users */
    private List<User> users = new ArrayList<>();

    /**
     * Initializes the admin controller.
     * Sets up the user table, loads users from the data manager,
     * and ensures the admin user is always present.
     */
    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        users.add(new User("admin"));
        users.addAll(0, DataManager.getAllUsers());
        userTableView.getItems().setAll(users);
    }

    /**
     * Handles creation of a new user.
     * Validates input, checks for duplicates, saves user via DataManager,
     * updates the user list and UI, and displays a status message.
     *
     * @throws IOException if saving the user fails
     */
    @FXML
    private void handleCreateUser() throws IOException {
        String username = newUsernameField.getText().trim();

        if (username.isEmpty()) {
            showStatus("Please enter a username", true);
            return;
        }

        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            showStatus("Username already exists", true);
            return;
        }

        User newUser = new User(username);
        DataManager.saveUser(newUser);
        users.add(newUser);
        userTableView.getItems().setAll(users);
        newUsernameField.clear();
        showStatus("User created successfully", false);
    }

    /**
     * Handles deletion of a selected user.
     * Prevents deletion of the "admin" user and updates the list and UI upon successful deletion.
     */
    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            showStatus("Please select a user to delete", true);
            return;
        }

        if (selectedUser.getUsername().equals("admin")) {
            showStatus("Cannot delete admin user", true);
            return;
        }

        users.remove(selectedUser);
        DataManager.removeUser(selectedUser.getUsername());
        userTableView.getItems().setAll(users);
        showStatus("User deleted successfully", false);
    }

    /**
     * Handles logout and navigates back to the login screen.
     * Displays an error message if the logout process fails.
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) userTableView.getScene().getWindow();
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Photo Album Login");
            stage.show();

        } catch (IOException e) {
            showStatus("Error during logout: " + e.getMessage(), true);
        }
    }

    /**
     * Closes the application window.
     */
    @FXML
    private void handleExit() {
        Stage stage = (Stage) userTableView.getScene().getWindow();
        stage.close();
    }

    /**
     * Displays a status message to the user.
     *
     * @param message  the message to display
     * @param isError  true if the message is an error, false otherwise
     */
    private void showStatus(String message, boolean isError) {
        statusLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.BLUE);
        statusLabel.setText(message);
    }
}
