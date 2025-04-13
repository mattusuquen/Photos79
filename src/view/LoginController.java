package src.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();

        if (username.isEmpty()) {
            errorLabel.setText("Please enter a username");
            return;
        }

        try {
            FXMLLoader loader;
            
            if (username.equals("admin")) loader = new FXMLLoader(getClass().getResource("/src/view/photos1.fxml"));
            else loader = new FXMLLoader(getClass().getResource("/src/view/admin.fxml"));

            Parent root = loader.load();
            
            // Get the current stage
            Stage stage = (Stage) usernameField.getScene().getWindow();
            
            // Create new scene with the main view
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Photo Album - " + username);
            stage.show();
            
        } catch (IOException e) {
            errorLabel.setText("Error loading application: " + e.getMessage());
        }
    }
} 