package src.app;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart.Data;
import javafx.stage.Stage;
import src.DataManager;
import src.User;

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
