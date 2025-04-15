package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user with a username, a list of albums, and tag types.
 * Implements Serializable to allow saving and loading user data.
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;
    private List<String> tagTypes;

    /**
     * Constructs a new User with the specified username.
     * Initializes an empty list of albums and a default list of tag types.
     * 
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<>();
        this.tagTypes = new ArrayList<>();
        tagTypes.add("location");
        tagTypes.add("person");
    }

    /**
     * Returns the username of the user.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the list of albums associated with the user.
     * 
     * @return The list of albums.
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * Adds an album to the user's list of albums, if it is not already present.
     * 
     * @param album The album to add.
     */
    public void addAlbum(Album album) {
        if (!albums.contains(album)) {
            albums.add(album);
        }
    }

    /**
     * Removes the specified album from the user's list of albums.
     * 
     * @param album The album to remove.
     */
    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    /**
     * Removes the album with the specified name from the user's list of albums.
     * 
     * @param name The name of the album to remove.
     */
    public void removeAlbum(String name) {
        Album album = getAlbumByName(name);
        if (album != null) {
            albums.remove(album);
        }
    }

    /**
     * Renames the album with the specified old name to a new name.
     * 
     * @param oldName The current name of the album.
     * @param newName The new name for the album.
     */
    public void renameAlbum(String oldName, String newName) {
        Album album = getAlbumByName(oldName);
        if (album != null) {
            album.setName(newName);
        }
    }

    /**
     * Retrieves an album by its name.
     * 
     * @param name The name of the album to search for.
     * @return The album if found, or null if no album with that name exists.
     */
    public Album getAlbumByName(String name) {
        for (Album album : albums) {
            if (album.getName().equalsIgnoreCase(name)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Checks if the user has an album with the specified name.
     * 
     * @param name The name of the album to check for.
     * @return true if the album exists, false otherwise.
     */
    public boolean hasAlbum(String name) {
        return getAlbumByName(name) != null;
    }

    /**
     * Returns a string representation of the user, including their username and the number of albums.
     * 
     * @return A string describing the user.
     */
    public String to_String() {
        return "User: " + username + " (Albums: " + albums.size() + ")";
    }

    /**
     * Returns a list of tag types associated with the user.
     * 
     * @return A list of tag types.
     */
    public List<String> getTagTypes() {
        return new ArrayList<>(tagTypes);
    }

    /**
     * Adds a new tag type to the user's list of tag types, ensuring it is not already present.
     * 
     * @param tagType The tag type to add.
     */
    public void addTagType(String tagType) {
        if (!tagTypes.contains(tagType.toLowerCase())) {
            tagTypes.add(tagType.toLowerCase());
        }
    }

    /**
     * Checks if the specified tag type is one that can have multiple values.
     * Currently, only "person" is considered to have multiple values.
     * 
     * @param tagType The tag type to check.
     * @return true if the tag type allows multiple values, false otherwise.
     */
    public boolean isTagTypeMultiple(String tagType) {
        return tagType.equalsIgnoreCase("person");
    }
}
