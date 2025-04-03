package src.view;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class Controller {
    @FXML
    private Label messageLabel;

    @FXML
    private Button showMessageButton;

    @FXML
    private void showMessage() {
        messageLabel.setText("Hello from PhotoApp!");
    }
    @FXML
    private ListView<String> albumListView;

    @FXML
    public void initialize() {
        ObservableList<String> albums = FXCollections.observableArrayList();
        albums.add("Album 1");
        albums.add("Album 2");
        albums.add("Album 3");
        albumListView.setItems(albums);
    }
}
