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
import src.User;
public class AdminController {
    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TextField newUsernameField;
    @FXML private Label statusLabel;
    
    private List<User> users = new ArrayList<>();
    
    @FXML
    public void initialize() {
        // Initialize table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        // Add admin user by default
        users.add(new User("admin"));
        
        // Populate table
        userTableView.getItems().setAll(users);
    }
    
    @FXML
    private void handleCreateUser() {
        String username = newUsernameField.getText().trim();
        
        if (username.isEmpty()) {
            showStatus("Please enter a username", true);
            return;
        }
        
        // Check if username already exists
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            showStatus("Username already exists", true);
            return;
        }
        
        // Create new user
        User newUser = new User(username);
        users.add(newUser);
        userTableView.getItems().setAll(users);
        newUsernameField.clear();
        showStatus("User created successfully", false);
    }
    
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
        userTableView.getItems().setAll(users);
        showStatus("User deleted successfully", false);
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) userTableView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Photo Album Login");
            stage.show();
            
        } catch (IOException e) {
            showStatus("Error during logout: " + e.getMessage(), true);
        }
    }
    
    @FXML
    private void handleExit() {
        Stage stage = (Stage) userTableView.getScene().getWindow();
        stage.close();
    }
    
    private void showStatus(String message, boolean isError) {
        statusLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.BLUE);
        statusLabel.setText(message);
    }
    
} 