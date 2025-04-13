package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

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

    private static boolean isUserDataFile(File file) {
        return file.isFile() && file.getName().endsWith(".dat");
    }



    private static User loadUserFromFile(File userFile) {
        try 
        {
            String username = userFile.getName().replace(".dat", "");
            return loadUser(username);
        } 
        
        catch (Exception e) 
        {
            System.err.println("Error loading user from file: " + userFile.getName());
            return null;
        }
    }


    
    public static Map<String, User> loadAllUsers() {
    
        Map<String, User> users = new HashMap<>();
    File dataDirectory = new File(DATA_DIR);
    
    if (!dataDirectory.exists() || !dataDirectory.isDirectory()) 
    {
        return users;  
    }
    
    for (File userFile : dataDirectory.listFiles()) {
        
        if (isUserDataFile(userFile)) 
        {
            User user = loadUserFromFile(userFile);
            
            if (user != null) 
            {
                users.put(user.getUsername(), user);
            }
        }
    }
    
    return users;
}
    

    
}
