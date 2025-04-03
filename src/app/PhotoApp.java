package src.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import src.view.Controller;

public class PhotoApp extends Application {
    Stage stage;
    public static void main(String[] args) { // Error: 'extends' is not allowed in the main method declaration
        launch(args);
    }

    @Override
    public void start(Stage s) throws Exception {
        this.stage = s;
        stage.setTitle("Photo Sharing App");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("view/photos.fxml"));
        //BorderPane root = (BorderPane)loader.load();
        //Controller controller = loader.getController();
        //controller.setMainStage(stage);
        //Scene scene = new Scene(root, 400, 300);
        //stage.setScene(scene);
        stage.show();
    }
}
