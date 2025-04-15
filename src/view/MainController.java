package src.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import src.Album;
import src.DataManager;
import src.User;

/**
 * Controller class for the main view of the photo album application.
 * Handles the interaction between the user and the list/table of albums.
 */
public class MainController {

    @FXML private ListView<String> albumListView;
    @FXML private TableView<Album> albumTableView;
    @FXML private TableColumn<Album, String> albumNameColumn;
    @FXML private TableColumn<Album, Integer> photoCountColumn;
    @FXML private TableColumn<Album, String> dateRangeColumn;

    private ObservableList<Album> albums = FXCollections.observableArrayList();
    private ObservableList<String> albumNames = FXCollections.observableArrayList();

    private User currentUser = DataManager.getCurrentUser();

    /**
     * Initializes the controller after the FXML components have been loaded.
     * Sets up table columns and populates album list and table with user data.
     */
    @FXML
    public void initialize() {
        albumNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        photoCountColumn.setCellValueFactory(new PropertyValueFactory<>("photoCount"));
        dateRangeColumn.setCellValueFactory(new PropertyValueFactory<>("dateRange"));

        for (Album album : currentUser.getAlbums()) {
            albums.add(album);
            albumNames.add(album.getName());
        }

        albumTableView.setItems(albums);
        albumListView.setItems(albumNames);
    }

    /**
     * Handles the creation of a new album through a text input dialog.
     */
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

    /**
     * Navigates to the search view for searching photos.
     */
    @FXML
    private void handleSearchPhotos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/search.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) albumListView.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Navigation Error", "Error opening search view: " + e.getMessage());
        }
    }

    /**
     * Displays an error alert dialog.
     *
     * @param title   the title of the alert
     * @param message the message to display
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles deletion of the selected album after user confirmation.
     */
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

    /**
     * Handles renaming the selected album through a text input dialog.
     */
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

    /**
     * Placeholder for album selection logic.
     */
    @FXML
    private void handleAlbumSelected() {
        // Logic for when an album is selected (if needed).
    }

    /**
     * Opens the selected album and navigates to the album view.
     */
    @FXML
    private void handleOpenAlbum() {
        String selectedAlbum = albumListView.getSelectionModel().getSelectedItem();
        if (selectedAlbum != null) {
            Album album = currentUser.getAlbumByName(selectedAlbum);
            if (album != null) {
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

    /**
     * Logs the user out and returns to the login screen.
     */
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) albumListView.getScene().getWindow();
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);
            stage.setTitle("Photo Album Login");
            stage.show();

            DataManager.saveCurrentUser();
            DataManager.setCurrentUser(null);
        } catch (IOException e) {
            showAlert("Error during logout: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    private void handleExit() {
        Stage stage = (Stage) albumListView.getScene().getWindow();
        stage.close();
    }

    /**
     * Adds a new album to the list and user's album collection.
     *
     * @param name the name of the new album
     */
    private void addAlbum(String name) {
        if (albumNames.contains(name)) {
            showAlert("Album already exists", Alert.AlertType.WARNING);
            return;
        }

        Album album = new Album(name);
        albums.add(album);
        albumNames.add(name);
        currentUser.addAlbum(album);
    }

    /**
     * Deletes an album from the list and the user's collection.
     *
     * @param name the name of the album to delete
     */
    private void deleteAlbum(String name) {
        albums.removeIf(album -> album.getName().equals(name));
        albumNames.remove(name);
        currentUser.removeAlbum(name);
    }

    /**
     * Renames an existing album.
     *
     * @param oldName the current name of the album
     * @param newName the new name to assign
     */
    private void renameAlbum(String oldName, String newName) {
        if (albumNames.contains(newName)) {
            showAlert("Album with this name already exists", Alert.AlertType.WARNING);
            return;
        }

        currentUser.renameAlbum(oldName, newName);

        for (Album album : albums) {
            if (album.getName().equals(oldName)) {
                album.setName(newName);
                break;
            }
        }

        int index = albumNames.indexOf(oldName);
        if (index >= 0) {
            albumNames.set(index, newName);
        }
    }

    /**
     * Displays a general alert with a specified message and alert type.
     *
     * @param message the message to display
     * @param type    the type of alert to display
     */
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Photo Album");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
