package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DataManager class is responsible for managing user data, including saving, loading, and removing user information,
 * as well as managing albums associated with the current user.
 */
public class DataManager {

    private static final String DATA_DIR = "data/"; // Directory to store user data files
    private static User currentUser; // The current user session

    /**
     * Gets the current user.
     * 
     * @return The current user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user.
     * 
     * @param currentUser The user to set as the current user.
     */
    public static void setCurrentUser(User currentUser) {
        DataManager.currentUser = currentUser;
    }

    /**
     * Saves the specified user to a file.
     * 
     * @param user The user to save.
     * @throws IOException If an I/O error occurs during the file operation.
     */
    public static void saveUser(User user) throws IOException {
        // Create the data directory if it does not exist
        new File(DATA_DIR).mkdirs();

        ObjectOutputStream oos = null;

        try {
            oos = new ObjectOutputStream(new FileOutputStream(DATA_DIR + user.getUsername() + ".dat"));
            oos.writeObject(user);
        } finally {
            if (oos != null) oos.close(); // Manual cleanup
        }
    }

    /**
     * Loads a user from a file based on their username.
     * 
     * @param username The username of the user to load.
     * @return The user object, or null if the user does not exist.
     * @throws IOException            If an I/O error occurs during the file operation.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static User loadUser(String username) throws IOException, ClassNotFoundException {
        File file = new File(DATA_DIR + username + ".dat");

        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (User) ois.readObject();
        }
    }

    /**
     * Checks if a file is a valid user data file (ends with ".dat").
     * 
     * @param file The file to check.
     * @return True if the file is a user data file, false otherwise.
     */
    private static boolean isUserDataFile(File file) {
        return file.isFile() && file.getName().endsWith(".dat");
    }

    /**
     * Removes a user file by username.
     * 
     * @param username The username of the user to remove.
     */
    public static void removeUser(String username) {
        File file = new File(DATA_DIR + username + ".dat");
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Loads a user from a file object.
     * 
     * @param userFile The file object that contains user data.
     * @return The user object, or null if an error occurs.
     */
    private static User loadUserFromFile(File userFile) {
        try {
            String username = userFile.getName().replace(".dat", "");
            return loadUser(username);
        } catch (Exception e) {
            System.err.println("Error loading user from file: " + userFile.getName());
            return null;
        }
    }

    /**
     * Saves the current user to a file.
     * 
     * @throws IOException If an I/O error occurs during the file operation.
     */
    public static void saveCurrentUser() throws IOException {
        if (currentUser != null) {
            saveUser(currentUser);
        }
    }

    /**
     * Loads all users from the data directory.
     * 
     * @return A map of username to user object.
     */
    public static Map<String, User> loadAllUsers() {
        Map<String, User> users = new HashMap<>();
        File dataDirectory = new File(DATA_DIR);

        if (!dataDirectory.exists() || !dataDirectory.isDirectory()) {
            return users;  
        }

        for (File userFile : dataDirectory.listFiles()) {
            if (isUserDataFile(userFile)) {
                User user = loadUserFromFile(userFile);

                if (user != null) {
                    users.put(user.getUsername(), user);
                }
            }
        }

        return users;
    }

    /**
     * Gets a list of all users.
     * 
     * @return A list of all user objects.
     */
    public static List<User> getAllUsers() {
        return new ArrayList<User>(loadAllUsers().values());
    }

    /**
     * Gets the albums of the current user.
     * 
     * @return A list of albums associated with the current user.
     */
    public static List<Album> getAlbums() {
        return currentUser.getAlbums();
    }

    /**
     * Saves the current user's album data.
     * 
     * @param album The album to save.
     * @throws IOException If an I/O error occurs during the file operation.
     */
    public static void saveAlbum(Album album) throws IOException {
        saveUser(currentUser);
    }
}
