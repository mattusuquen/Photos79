package src;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;
    private List<String> tagTypes;

    public User(String username)
    {
        this.username = username;
        this.albums = new ArrayList<>();
        this.tagTypes = new ArrayList<>();
        tagTypes.add("location");
        tagTypes.add("person");
    }

    public String getUsername() 
    {
        return username;
    }
    
    public List<Album> getAlbums() 
    {
        return albums;
    }

    public void addAlbum(Album album) 
    {
        if (!albums.contains(album)) 
        {
            albums.add(album);
        }
    }

    public void removeAlbum(Album album) 
    {
        albums.remove(album);
    }


    public Album getAlbumByName(String name) 
    {
        for (Album album : albums) 
        {
            if (album.getName().equalsIgnoreCase(name)) 
            {
                return album;
            }
        }
        return null;
    }

    public boolean hasAlbum(String name) 
    {
        return getAlbumByName(name) != null;
    }

    public String to_String() 
    {
        return "User: " + username + " (Albums: " + albums.size() + ")";
    }

    public List<String> getTagTypes() {
        return new ArrayList<>(tagTypes);
    }

    public void addTagType(String tagType) {
        
        if (!tagTypes.contains(tagType.toLowerCase())) 
        {
            tagTypes.add(tagType.toLowerCase());
        }
    }

    public boolean isTagTypeMultiple(String tagType) {
 
        return tagType.equalsIgnoreCase("person");
    }
}
