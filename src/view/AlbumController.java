package src.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import src.Album;
import src.DataManager;
import src.Photo;
import src.Tag;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class AlbumController {

    @FXML private Label albumNameLabel;
    @FXML private TilePane photoGrid;
    @FXML private ImageView displayedImageView;
    @FXML private Label captionLabel;
    @FXML private Label dateLabel;
    @FXML private Label photoIndexLabel;
    @FXML private ListView<String> tagListView;
    @FXML private ComboBox<String> tagTypeComboBox;
    @FXML private TextField tagValueField;

    private Album currentAlbum;
    private Photo currentPhoto;
    private int currentPhotoIndex = -1;
    private ObservableList<Photo> photos = FXCollections.observableArrayList();
    private ObservableList<Tag> tags = FXCollections.observableArrayList();
    
    // Properties for binding UI elements
    private final BooleanProperty noPhotoSelected = new SimpleBooleanProperty(true);
    private final BooleanProperty noPhotos = new SimpleBooleanProperty(true);
    private final BooleanProperty noTagSelected = new SimpleBooleanProperty(true);

    public BooleanProperty noPhotoSelectedProperty() {
        return noPhotoSelected;
    }

    public BooleanProperty noPhotosProperty() {
        return noPhotos;
    }

    public BooleanProperty noTagSelectedProperty() {
        return noTagSelected;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        // Initialize tag types
        tagTypeComboBox.setItems(FXCollections.observableArrayList("Location", "Person", "Event", "Other"));
        ObservableList<String> tagNames = FXCollections.observableArrayList("Location", "Person", "Event", "Other");
        // Set up tag ListView
        tagListView.setItems(tagNames);
        tagListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> noTagSelected.set(newVal == null));
        
        // Initial state
        noPhotoSelected.set(true);
        noPhotos.set(true);
        noTagSelected.set(true);
    }

    /**
     * Sets the album to be displayed.
     * @param album The album to display
     */
    public void setAlbum(Album album) {
        this.currentAlbum = album;
        albumNameLabel.setText(album.getName());
        
        // Load photos
        photos.clear();
        photos.addAll(album.getPhotos());
        noPhotos.set(photos.isEmpty());
        
        // Update UI
        refreshPhotoGrid();
        
        // Select the first photo if available
        if (!photos.isEmpty()) {
            selectPhoto(0);
        } else {
            clearPhotoDisplay();
        }
    }

    /**
     * Refreshes the photo grid with current photos.
     */
    private void refreshPhotoGrid() {
        photoGrid.getChildren().clear();
        
        for (int i = 0; i < photos.size(); i++) {
            Photo photo = photos.get(i);
            
            // Create thumbnail
            ImageView thumbnailView = new ImageView(photo.getImage());
            thumbnailView.setFitHeight(100);
            thumbnailView.setFitWidth(100);
            thumbnailView.setPreserveRatio(true);
            
            // Create caption label
            Label captionLabel = new Label(photo.getCaption());
            captionLabel.setWrapText(true);
            captionLabel.setMaxWidth(100);
            
            // Create container for thumbnail and caption
            VBox photoBox = new VBox(5);
            photoBox.setAlignment(Pos.CENTER);
            photoBox.getChildren().addAll(thumbnailView, captionLabel);
            
            // Set selection style
            if (currentPhotoIndex == i) {
                photoBox.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5px;");
            } else {
                photoBox.setStyle("-fx-padding: 5px;");
            }
            
            // Add click handler
            final int index = i;
            photoBox.setOnMouseClicked(event -> selectPhoto(index));
            
            photoGrid.getChildren().add(photoBox);
        }
        
        // Update photo index display
        updatePhotoIndexLabel();
    }

    /**
     * Selects a photo by index.
     * @param index The index of the photo to select
     */
    private void selectPhoto(int index) {
        if (index >= 0 && index < photos.size()) {
            currentPhotoIndex = index;
            currentPhoto = photos.get(index);
            
            // Update display
            displayedImageView.setImage(currentPhoto.getImage());
            captionLabel.setText(currentPhoto.getCaption());
            Calendar dateTaken = currentPhoto.getDateTaken();
            int year = dateTaken.get(Calendar.YEAR);
            int month = dateTaken.get(Calendar.MONTH) + 1; // 0-based, so add 1
            int day = dateTaken.get(Calendar.DAY_OF_MONTH);
            int hour = dateTaken.get(Calendar.HOUR_OF_DAY);
            int minute = dateTaken.get(Calendar.MINUTE);
            int second = dateTaken.get(Calendar.SECOND);

            // Format to string manually (zero-padding optional)
            String formattedDate = String.format("%04d-%02d-%02d %02d:%02d:%02d",
                                                year, month, day, hour, minute, second);
            dateLabel.setText(formattedDate);
            
            // Update tags
            tags.clear();
            tags.addAll(currentPhoto.getTags());
            
            // Update properties
            noPhotoSelected.set(false);
            
            // Refresh grid to show selection
            refreshPhotoGrid();
        }
    }

    /**
     * Clears the photo display area.
     */
    private void clearPhotoDisplay() {
        currentPhoto = null;
        currentPhotoIndex = -1;
        displayedImageView.setImage(null);
        captionLabel.setText("");
        dateLabel.setText("");
        tags.clear();
        noPhotoSelected.set(true);
        refreshPhotoGrid();
    }

    /**
     * Updates the photo index label.
     */
    private void updatePhotoIndexLabel() {
        if (photos.isEmpty()) {
            photoIndexLabel.setText("0/0");
        } else {
            photoIndexLabel.setText((currentPhotoIndex + 1) + "/" + photos.size());
        }
    }

    /**
     * Handles the action to add a new photo.
     */
    @FXML
    private void handleAddPhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        
        File selectedFile = fileChooser.showOpenDialog(photoGrid.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Create new photo
                Photo newPhoto = new Photo(selectedFile.getAbsolutePath(), Calendar.getInstance());
                
                // Add to album
                currentAlbum.addPhoto(newPhoto);
                photos.add(newPhoto);
                noPhotos.set(false);
                
                // Select the new photo
                selectPhoto(photos.size() - 1);
                
                // Show dialog for caption
                showCaptionDialog(newPhoto);
            } catch (Exception e) {
                showErrorAlert("Error adding photo", e.getMessage());
            }
        }
    }

    /**
     * Handles the action to remove a photo.
     */
    @FXML
    private void handleRemovePhoto() {
        if (currentPhoto == null) return;
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Photo");
        alert.setContentText("Are you sure you want to delete this photo from the album?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int indexToRemove = currentPhotoIndex;
            
            // Remove from album
            currentAlbum.removePhoto(currentPhoto);
            photos.remove(currentPhoto);
            
            // Update UI
            noPhotos.set(photos.isEmpty());
            
            if (photos.isEmpty()) {
                clearPhotoDisplay();
            } else {
                // Select next available photo
                int newIndex = Math.min(indexToRemove, photos.size() - 1);
                selectPhoto(newIndex);
            }
        }
    }

    /**
     * Handles the action to caption/recaption a photo.
     */
    @FXML
    private void handleCaptionPhoto() {
        if (currentPhoto == null) return;
        showCaptionDialog(currentPhoto);
    }

    /**
     * Shows a dialog to enter or edit a photo caption.
     * @param photo The photo to caption
     */
    private void showCaptionDialog(Photo photo) {
        TextInputDialog dialog = new TextInputDialog(photo.getCaption());
        dialog.setTitle("Photo Caption");
        dialog.setHeaderText("Enter caption for the photo:");
        dialog.setContentText("Caption:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(caption -> {
            photo.setCaption(caption);
            
            // Update UI if this is the current photo
            if (photo.equals(currentPhoto)) {
                captionLabel.setText(caption);
            }
            
            // Refresh photo grid to update captions
            refreshPhotoGrid();
        });
    }

    /**
     * Handles the action to copy a photo to another album.
     */
    @FXML
    private void handleCopyPhoto() {
        if (currentPhoto == null) return;
        
        // Show album selection dialog
        Album targetAlbum = showSelectAlbumDialog("Copy Photo", "Select destination album:");
        if (targetAlbum != null && targetAlbum != currentAlbum) {
            // Copy photo to target album
            targetAlbum.addPhoto(currentPhoto);
            showInfoAlert("Photo Copied", "Photo has been copied to album: " + targetAlbum.getName());
        }
    }

    /**
     * Handles the action to move a photo to another album.
     */
    @FXML
    private void handleMovePhoto() {
        if (currentPhoto == null) return;
        
        // Show album selection dialog
        Album targetAlbum = showSelectAlbumDialog("Move Photo", "Select destination album:");
        if (targetAlbum != null && targetAlbum != currentAlbum) {
            // Move photo to target album
            targetAlbum.addPhoto(currentPhoto);
            
            // Remove from current album
            currentAlbum.removePhoto(currentPhoto);
            photos.remove(currentPhoto);
            
            // Update UI
            noPhotos.set(photos.isEmpty());
            
            if (photos.isEmpty()) {
                clearPhotoDisplay();
            } else {
                // Select next available photo
                int newIndex = Math.min(currentPhotoIndex, photos.size() - 1);
                selectPhoto(newIndex);
            }
            
            showInfoAlert("Photo Moved", "Photo has been moved to album: " + targetAlbum.getName());
        }
    }

    /**
     * Shows a dialog to select an album.
     * @param title Dialog title
     * @param headerText Dialog header text
     * @return The selected album, or null if cancelled
     */
    private Album showSelectAlbumDialog(String title, String headerText) {
        // Get all albums from the application
        List<Album> albums = DataManager.getAlbums();
        
        // Create dialog
        Dialog<Album> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);
        
        // Set the button types
        ButtonType selectButtonType = new ButtonType("Select", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(selectButtonType, ButtonType.CANCEL);
        
        // Create ListView for albums
        ListView<Album> albumListView = new ListView<>();
        albumListView.setItems(FXCollections.observableArrayList(albums));
        albumListView.setCellFactory(param -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                
                if (empty || album == null) {
                    setText(null);
                } else {
                    setText(album.getName());
                    if (album.equals(currentAlbum)) {
                        setStyle("-fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        
        // Set dialog content
        dialog.getDialogPane().setContent(albumListView);
        
        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == selectButtonType) {
                return albumListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });
        
        // Show dialog and get result
        return dialog.showAndWait().orElse(null);
    }

    /**
     * Handles the action to go to the previous photo.
     */
    @FXML
    private void handlePreviousPhoto() {
        if (photos.isEmpty()) return;
        
        int newIndex = (currentPhotoIndex - 1 + photos.size()) % photos.size();
        selectPhoto(newIndex);
    }

    /**
     * Handles the action to go to the next photo.
     */
    @FXML
    private void handleNextPhoto() {
        if (photos.isEmpty()) return;
        
        int newIndex = (currentPhotoIndex + 1) % photos.size();
        selectPhoto(newIndex);
    }

    /**
     * Handles the action to add a tag to the current photo.
     */
    @FXML
    private void handleAddTag() {
        if (currentPhoto == null) return;
        
        String tagType = tagTypeComboBox.getValue();
        String tagValue = tagValueField.getText().trim();
        
        if (tagType == null || tagType.isEmpty()) {
            showErrorAlert("Missing Tag Type", "Please select a tag type.");
            return;
        }
        
        if (tagValue.isEmpty()) {
            showErrorAlert("Missing Tag Value", "Please enter a tag value.");
            return;
        }
        Tag tag = new Tag(tagType,tagValue);
        // Check if tag already exists
        if (currentPhoto.getTags().contains(tag)) {
            showErrorAlert("Duplicate Tag", "This tag already exists for this photo.");
            return;
        }
        
        // Add tag to photo
        currentPhoto.addTag(tag);
        
        // Update UI
        tags.add(tag);
        
        // Clear input fields
        tagTypeComboBox.getSelectionModel().clearSelection();
        tagValueField.clear();
    }

    /**
     * Handles the action to delete a tag from the current photo.
     */
    @FXML
    private void handleDeleteTag() {
        if (currentPhoto == null) return;
        
        String selectedTag = tagListView.getSelectionModel().getSelectedItem();
        if (selectedTag == null) return;
        
        // Remove tag from photo
        //currentPhoto.removeTag(selectedTag);
        
        // Update UI
        tags.remove(selectedTag);
    }

    /**
     * Handles the action to go back to the albums view.
     */
    @FXML
    private void handleBackToAlbums() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();
            
            // Set the scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) albumNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            DataManager.saveCurrentUser();
        } catch (IOException e) {
            showErrorAlert("Navigation Error", "Error returning to albums view: " + e.getMessage());
        }
    }

    /**
     * Shows an error alert.
     * @param title Alert title
     * @param message Alert message
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert.
     * @param title Alert title
     * @param message Alert message
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}