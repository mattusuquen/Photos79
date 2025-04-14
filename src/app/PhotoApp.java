package src.app;
import java.io.Serializable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
<<<<<<< HEAD
import src.DataManager;
=======
>>>>>>> c4155be8eda3e721385f0f92d01cf7957d3fb956

public class PhotoApp extends Application implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String storeDir = "dat";
    public static final String storeFile = "photos.dat";

    public void writeApp() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        out.writeObject(this);
        out.close();
    }

    public static PhotoApp readApp() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        return (PhotoApp) in.readObject();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/src/view/login.fxml"));
        Parent root = loader.load();
        
        primaryStage.setTitle("Photo Album Login");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        try {
            PhotoApp app = PhotoApp.readApp();
            app.launch(args);
            app.writeApp();
        } catch (IOException | ClassNotFoundException e) {
            // If there's an error reading the saved state, start fresh
            launch(args);
        }
    }
}
