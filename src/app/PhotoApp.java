package src.app;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PhotoApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/photos1.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Photo App");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
