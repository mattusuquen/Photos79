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

/**
 * Represents a photo with its file path, caption, date taken, tags, and image.
 * Implements Serializable interface for saving photo state.
 */
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String filePath;
    private String caption;
    private Calendar dateTaken;
    private List<Tag> tags;
    private transient Image image;

    /**
     * Constructor to initialize a Photo object with file path and date taken.
     * 
     * @param filePath the file path of the photo
     * @param dateTaken the date when the photo was taken
     */
    public Photo(String filePath, Calendar dateTaken) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = "";
        this.tags = new ArrayList<>();
        //image = new Image(new FileInputStream(filePath));
    }

    /**
     * Returns the File object representing the photo.
     * 
     * @return the File object for the photo
     */
    public File getFile() {
        return new File(filePath);
    }

    /**
     * Returns the file path of the photo.
     * 
     * @return the file path as a string
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns the caption of the photo.
     * 
     * @return the caption as a string
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Returns the date when the photo was taken.
     * 
     * @return the date taken as a Calendar object
     */
    public Calendar getDateTaken() {
        return dateTaken;
    }

    /**
     * Returns the Image object representing the photo.
     * Attempts to load the image from the file path.
     * 
     * @return the Image object or null if an error occurs
     */
    public Image getImage() {
        try {
            return new Image(new FileInputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns a list of tags associated with the photo.
     * 
     * @return a List of Tag objects
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Returns a list of tag names as strings.
     * 
     * @return a List of tag names as strings
     */
    public List<String> readTags() {
        List<String> tagList = new ArrayList<>();
        for (Tag tag : tags) {
            tagList.add(tag.to_String());
        }
        return tagList;
    }

    /**
     * Returns a specific tag at the given index.
     * 
     * @param index the index of the tag in the list
     * @return the Tag object at the specified index
     */
    public Tag getTag(int index) {
        return tags.get(index);
    }

    /**
     * Adds a new tag to the photo.
     * 
     * @param name the name of the tag
     * @param value the value of the tag
     */
    public void addTag(String name, String value) {
        Tag tag = new Tag(name, value);
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    /**
     * Removes the specified tag from the photo.
     * 
     * @param tag the tag to be removed
     */
    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

    /**
     * Removes the tag with the specified name and value from the photo.
     * 
     * @param name the name of the tag to be removed
     * @param value the value of the tag to be removed
     */
    public void removeTag(String name, String value) {
        Tag tag = new Tag(name, value);
        tags.remove(tag);
    }

    /**
     * Sets the caption of the photo.
     * 
     * @param caption the caption to be set
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Adds an existing tag to the photo if it doesn't already exist.
     * 
     * @param tag the tag to be added
     */
    public void addTag(Tag tag) {
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
    }

    /**
     * Compares this photo to another object for equality based on file path.
     * 
     * @param o the object to compare
     * @return true if the two objects are equal, false otherwise
     */
    public boolean Equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;
        return filePath.equals(photo.filePath);
    }

    /**
     * Returns a hash code value for the photo based on its file path.
     * 
     * @return a hash code value for the photo
     */
    public int hash_Code() {
        return Objects.hash(filePath);
    }

    /**
     * Returns a string representation of the photo, including its file path and caption.
     * 
     * @return a string representation of the photo
     */
    public String to_String() {
        return "Photo: " + filePath + ", caption: " + caption;
    }

}
