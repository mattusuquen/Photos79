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

public class SearchController {

    @FXML private TilePane photoGrid;
    @FXML private ImageView displayedImageView;
    @FXML private Label captionLabel;
    @FXML private Label dateLabel;
    @FXML private ListView<String> tagListView;
    @FXML private DatePicker fromDatePicker;
    @FXML private DatePicker toDatePicker;
    @FXML private TextField tag1TypeField;
    @FXML private TextField tag1ValueField;
    @FXML private TextField tag2TypeField;
    @FXML private TextField tag2ValueField;
    @FXML private ComboBox<String> tagComboBox;

    private Photo currentPhoto;
    private int currentPhotoIndex = -1;
    private ObservableList<Photo> photos = FXCollections.observableArrayList();
    private ObservableList<String> tagStrings = FXCollections.observableArrayList();
    
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
        for (Album album : DataManager.getCurrentUser().getAlbums()) {
            photos.addAll(album.getPhotos());
        }
        tagComboBox.getItems().addAll("AND","OR"); // Example tags, replace with actual tags
        tagComboBox.getSelectionModel().selectFirst(); // Default selection
        // Set up tag ListView
        tagListView.setItems(tagStrings);
        tagListView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> noTagSelected.set(newVal == null));
        
        // Initial state
        noPhotoSelected.set(true);
        noPhotos.set(true);
        noTagSelected.set(true);
        refreshPhotoGrid();
    }
    

    public ObservableList<Photo> loadPhotos() {
        ObservableList<Photo> allUserPhotos = FXCollections.observableArrayList();
        for (Album album : DataManager.getCurrentUser().getAlbums()) {
            allUserPhotos.addAll(album.getPhotos());
        }
        return allUserPhotos;
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
            refreshTagList();
            
            // Update properties
            noPhotoSelected.set(false);
            
            // Refresh grid to show selection
            refreshPhotoGrid();
        }
    }

    /**
     * Refreshes the tag list with current photo's tags.
     */
    private void refreshTagList() {
        tagStrings.clear();
        if (currentPhoto != null) {
            for (Tag tag : currentPhoto.getTags()) {
                tagStrings.add(tag.getName() + ": " + tag.getValue());
            }
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
        tagStrings.clear();
        noPhotoSelected.set(true);
        refreshPhotoGrid();
    }


    @FXML 
    private void handleSearch(){
        Calendar fromDate = null;
        Calendar toDate = null;
        String tag1Type = tag1TypeField.getText().trim();
        String tag1Value = tag1ValueField.getText().trim();
        String tag2Type = tag2TypeField.getText().trim();
        String tag2Value = tag2ValueField.getText().trim();
        if (fromDatePicker.getValue() != null) {
            fromDate = Calendar.getInstance();
            fromDate.set(fromDatePicker.getValue().getYear(), fromDatePicker.getValue().getMonthValue() - 1, fromDatePicker.getValue().getDayOfMonth(), 0, 0, 0);
            fromDate.set(Calendar.MILLISECOND, 0);
        }
        if (toDatePicker.getValue() != null) {
            toDate = Calendar.getInstance();
            toDate.set(toDatePicker.getValue().getYear(), toDatePicker.getValue().getMonthValue() - 1, toDatePicker.getValue().getDayOfMonth(), 23, 59, 59);
            toDate.set(Calendar.MILLISECOND, 999);
        }
        if (!tag1Type.isEmpty() && tag1Value.isEmpty()) {
            showAlert("Missing Tag Type","Please enter a value for the tag 1 type");
            return;
        }
        if (tag1Type.isEmpty() && !tag1Value.isEmpty()) {
            showAlert("Missing Tag Value","Please enter a value for the tag 1 value");
            return;
        }
        if (!tag2Type.isEmpty() && tag2Value.isEmpty()) {
            showAlert("Missing Tag Type","Please enter a value for the tag 2 type");
            return;
        }
        if (tag2Type.isEmpty() && !tag2Value.isEmpty()) {
            showAlert("Missing Tag Value","Please enter a value for the tag 2 value");
            return;
        }
        if (fromDate != null && toDate != null && fromDate.compareTo(toDate) > 0) {
            showAlert("Invalid Date Range","The 'From' date must be before the 'To' date");
            return;
        }
        photos.clear();
        tagStrings.clear();
        for (Photo photo : loadPhotos()){
            boolean matches = true;
            Calendar photoDate = photo.getDateTaken();
            if (fromDate != null && photoDate.compareTo(fromDate) < 0) {
                matches = false;
            }
            if (toDate != null && photoDate.compareTo(toDate) > 0) {
                matches = false;
            }
            if (!tag1Type.isEmpty() && !tag1Value.isEmpty() && tag2Type.isEmpty() && tag2Value.isEmpty()) {
                Tag tag1 = new Tag(tag1Type, tag1Value);
                if(!photo.getTags().contains(tag1)) matches = false;
            } else if (tag1Type.isEmpty() && tag1Value.isEmpty() && !tag2Type.isEmpty() && !tag2Value.isEmpty()) {
                Tag tag2 = new Tag(tag2Type, tag2Value);
                if(!photo.getTags().contains(tag2)) matches = false;
            } else {
                Tag tag1 = new Tag(tag1Type, tag1Value);
                Tag tag2 = new Tag(tag2Type, tag2Value);
                if (tagComboBox.getSelectionModel().getSelectedItem().equals("AND")) {
                    if (!photo.getTags().contains(tag1) || !photo.getTags().contains(tag2)) matches = false;
                } else {
                    if (!photo.getTags().contains(tag1) && !photo.getTags().contains(tag2)) matches = false;
                }
            }

            if (matches) {
                photos.add(photo);
            }
        }
        refreshPhotoGrid();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleClearSearch() {
        fromDatePicker.setValue(null);
        toDatePicker.setValue(null);
        tag1TypeField.clear();
        tag1ValueField.clear();
        tag2TypeField.clear();
        tag2ValueField.clear();
        photos.clear();
        tagStrings.clear();
        noPhotoSelected.set(true);
        photos = loadPhotos();
        currentPhoto = null;
        refreshPhotoGrid();
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
     * Handles the action to go back to the albums view.
     */
    @FXML
    private void handleBackToAlbums() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();
            
            // Set the scene
            Scene scene = new Scene(root);
            Stage stage = (Stage) photoGrid.getScene().getWindow();
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
}