package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class DataManager {
    private static final String DATA_DIR = "data/";

    public static void saveUser(User user) throws IOException {
        
        new File(DATA_DIR).mkdirs();
        
        ObjectOutputStream oos = null;
        
        try 
        {
            oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + user.getUsername() + ".dat"));
            oos.writeObject(user);
        } 
        
        finally 
        {
            if (oos != null) oos.close(); // Manual cleanup
        }

    }

    public static User loadUser(String username) throws IOException, ClassNotFoundException {
        
        File file = new File(DATA_DIR + username + ".dat");
        
        if (!file.exists()) return null;
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) 
        {
            return (User) ois.readObject();
        }
    }
}
