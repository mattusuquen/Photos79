package src.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import src.DataManager;
import src.User;

import java.io.IOException;

/**
 * Controller for handling user login functionality.
 * This class is responsible for validating user input, loading the appropriate view,
 * and managing user login actions.
 */
public class LoginController {

    @FXML private TextField usernameField; // Text field for entering the username.
    @FXML private Label errorLabel; // Label for displaying error messages.

    /**
     * Handles the login process when the user submits their username.
     * Validates the input and either loads the admin view or the main view based on the username.
     * 
     * @throws ClassNotFoundException if there is an error while loading user data.
     */
    @FXML
    private void handleLogin() throws ClassNotFoundException {
        // Retrieve the username entered by the user
        String username = usernameField.getText();

        // Validate the username input
        if (username.isEmpty()) {
            errorLabel.setText("Please enter a username");
            return;
        }

        try {
            FXMLLoader loader = null;

            // If the username is "admin", load the admin view
            if (username.equals("admin")) {
                loader = new FXMLLoader(getClass().getResource("/src/view/admin.fxml"));
            } else {
                try {
                    // Check if the user exists in the system
                    if (DataManager.loadUser(username) == null) {
                        errorLabel.setText("Username does not exist.");
                        return;
                    }
                } catch (ClassNotFoundException e) {
                    // Handle error if user data cannot be loaded
                    errorLabel.setText("Error: User data could not be loaded.");
                }
                // Load the main view for regular users
                loader = new FXMLLoader(getClass().getResource("/src/view/main.fxml"));
            }

            // Load the user data for the current user
            User currentUser = DataManager.loadUser(username);
            DataManager.setCurrentUser(currentUser);

            // Load the appropriate view
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) usernameField.getScene().getWindow();

            // Create new scene with the main view
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Photo Album - " + username);
            stage.show();

        } catch (IOException e) {
            // Handle error if there is an issue loading the application
            errorLabel.setText("Error loading application: " + e.getMessage());
        }
    }
}
