package src.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import src.Album;
import src.DataManager;
import src.User;
public class MainController {
    @FXML private ListView<String> albumListView;
    @FXML private TableView<Album> albumTableView;
    @FXML private TableColumn<Album, String> albumNameColumn;
    @FXML private TableColumn<Album, Integer> photoCountColumn;
    @FXML private TableColumn<Album, String> dateRangeColumn;
    
    private ObservableList<Album> albums = FXCollections.observableArrayList();
    private ObservableList<String> albumNames = FXCollections.observableArrayList();
    
    private User currentUser = DataManager.getCurrentUser();

    @FXML
    public void initialize() {
        // Initialize table columns
        albumNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        photoCountColumn.setCellValueFactory(new PropertyValueFactory<>("photoCount"));
        dateRangeColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));
        
        for (Album album : currentUser.getAlbums()) {
            albums.add(album);
            albumNames.add(album.getName());
        }
        
        // Set up the table view
        albumTableView.setItems(albums);
        
        // Set up the list view
        albumListView.setItems(albumNames);
    }
    
    @FXML
    private void handleCreateAlbum() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Album");
        dialog.setHeaderText("Enter album name");
        dialog.setContentText("Name:");
        
        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                addAlbum(name.trim());
            }
        });
    }
    
    @FXML
    private void handleDeleteAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        
        if (selectedAlbum == null) {
            showAlert("Please select an album to delete", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Album");
        confirmDialog.setHeaderText("Are you sure you want to delete this album?");
        confirmDialog.setContentText("Album: " + selectedAlbum);
        
        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteAlbum(selectedAlbum);
            }
        });
    }
    
    @FXML
    private void handleRenameAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        
        if (selectedAlbum == null) {
            showAlert("Please select an album to rename", Alert.AlertType.WARNING);
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(selectedAlbum);
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Enter new album name");
        dialog.setContentText("Name:");
        
        dialog.showAndWait().ifPresent(newName -> {
            if (!newName.trim().isEmpty() && !newName.equals(selectedAlbum)) {
                renameAlbum(selectedAlbum, newName.trim());
            }
        });
    }
    
    @FXML
    private void handleAlbumSelected() {
        /*
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            Album album = currentUser.getAlbumByName(selectedAlbum);
            if (album != null) {
                // Load the album view
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/album.fxml"));
                    Parent root = loader.load();
                    
                    AlbumController controller = loader.getController();
                    controller.setAlbum(album);
                    
                    Stage stage = (Stage) albumListView.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Photo Album - " + selectedAlbum);
                    stage.show();
                } catch (IOException e) {
                    showAlert("Error loading album: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
         */
        
    }
    
    @FXML
    private void handleOpenAlbum(){
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            Album album = currentUser.getAlbumByName(selectedAlbum);
            if (album != null) {
                // Load the album view
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/album.fxml"));
                    Parent root = loader.load();
                    
                    AlbumController controller = loader.getController();
                    controller.setAlbum(album);
                    
                    Stage stage = (Stage) albumListView.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Photo Album - " + selectedAlbum);
                    stage.show();
                    DataManager.saveCurrentUser();
                } catch (IOException e) {
                    showAlert("Error loading album: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) albumListView.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Photo Album Login");
            stage.show();
            DataManager.saveCurrentUser();
            DataManager.setCurrentUser(null);
        } catch (IOException e) {
            showAlert("Error during logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleExit() {
        Stage stage = (Stage) albumListView.getScene().getWindow();
        stage.close();
    }
    
    private void addAlbum(String name) {
        // Check if album already exists
        if (albumNames.contains(name)) {
            showAlert("Album already exists", Alert.AlertType.WARNING);
            return;
        }
        
        // Create new album
        Album album = new Album(name);
        albums.add(album);
        albumNames.add(name);
        currentUser.addAlbum(album);
    }
    
    private void deleteAlbum(String name) {
        // Find and remove the album
        albums.removeIf(album -> album.getName().equals(name));
        albumNames.remove(name);
        currentUser.removeAlbum(name);
    }
    
    private void renameAlbum(String oldName, String newName) {
        // Check if new name already exists
        if (albumNames.contains(newName)) {
            showAlert("Album with this name already exists", Alert.AlertType.WARNING);
            return;
        }
        
        currentUser.renameAlbum(oldName, newName);

        // Find and update the album
        for (Album album : albums) {
            if (album.getName().equals(oldName)) {
                album.setName(newName);
                break;
            }
        }
        
        // Update the list view
        int index = albumNames.indexOf(oldName);
        if (index >= 0) {
            albumNames.set(index, newName);
        }
    }
    
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Photo Album");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
