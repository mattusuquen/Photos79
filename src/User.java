package src;

import java.util.ArrayList;
import java.util.List;

public class User {
    
    private String username;
    private List<Album> albums;

    public User(String username)
    {
        this.username = username;
        this.albums = new ArrayList<>();
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
}
