package src;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import javafx.scene.image.Image;

public class Photo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private String filePath;
    private String caption;
    private Calendar dateTaken;
    private List<Tag> tags;
    private Image image;

    public Photo(String filePath, Calendar dateTaken) throws FileNotFoundException {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = "";
        this.tags = new ArrayList<>();
        image = new Image(new FileInputStream(filePath));
    }
    public File getFile() {
        return new File(filePath);
    }
    public String getFilePath() 
    {
        return filePath;
    }

    public String getCaption() 
    {
        return caption;
    }

    public Calendar getDateTaken() 
    {
        return dateTaken;
    }
    public Image getImage() {
        return image;
    }
    public List<Tag> getTags() 
    {
        return tags;
    }
    public List<String> readTags(){
        List<String> tagList = new ArrayList<>();
        for (Tag tag : tags) {
            tagList.add(tag.to_String());
        }
        return tagList;
    }
    public Tag getTag(int index) 
    {
        return tags.get(index);
    }
    public void addTag(String name, String value) 
    {
        Tag tag = new Tag(name, value);
        if (!tags.contains(tag)) 
        {
            tags.add(tag);
        }
    }
    public void removeTag(Tag tag) 
    {
        tags.remove(tag);
    }
    public void removeTag(String name, String value) 
    {
        Tag tag = new Tag(name, value);
        tags.remove(tag);
    }
    public void setCaption(String caption) 
    {
        this.caption = caption;
    }

    public void addTag(Tag tag) 
    {
        if (!tags.contains(tag)) 
        {
            tags.add(tag);
        }
    }

 
    public boolean Equals(Object o) 
    {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;
        
        Photo photo = (Photo) o;
        return filePath.equals(photo.filePath);
    }

    public int hash_Code() 
    {
        return Objects.hash(filePath);
    }

    public String to_String() 
    {
        return "Photo: " + filePath + ", caption: " + caption;
    }
    
}
