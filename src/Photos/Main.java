package src.Photos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.DataManager;

/**
 * The {@code PhotoApp} class serves as the entry point of the JavaFX photo album application.
 * It initializes the primary stage and loads the login screen.
 * Upon application shutdown, it saves the current user's data.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by loading the login screen and setting the initial stage.
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if an error occurs during loading the FXML file or setting up the stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Photo Album Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    /**
     * Called when the application is about to stop.
     * Saves the current user's data using {@link DataManager#saveCurrentUser()}.
     *
     * @throws Exception if an error occurs during saving user data.
     */
    @Override
    public void stop() throws Exception {
        DataManager.saveCurrentUser();
    }

    /**
     * The main method serves as the entry point to launch the JavaFX application.
     *
     * @param args the command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
