package src.app;
import java.io.Serializable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.DataManager;

public class PhotoApp extends Application implements Serializable {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Photo Album Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
    @Override
    public void stop() throws Exception {
        DataManager.saveCurrentUser();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
