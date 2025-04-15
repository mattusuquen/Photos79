package src;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

/**
 * Represents an album containing a collection of photos.
 * Allows for managing photos, updating album name, filtering, and navigating through photos.
 */
public class Album implements Serializable {
    
    private static final long serialVersionUID = 1L; 
    private String name;
    private List<Photo> photos;
    private int index = 0;
    private String dateRange = "HI"; 
    
    /**
     * Constructs an Album with the specified name.
     * Initializes an empty list of photos.
     * 
     * @param name the name of the album
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
        this.dateRange = "N/A";
    }

    /**
     * Returns the number of photos in the album.
     * 
     * @return the size of the photos list
     */
    public int getPhotoCount() {
        return photos.size();
    }

    /**
     * Returns the date range for the album based on the photos' capture dates.
     * If there are no photos, returns "N/A".
     * 
     * @return the date range of the photos in the album
     */
    public String getDateRange(){
        if (photos.isEmpty()) 
        {
            return "N/A";
        } else if (photos.size() == 1) 
        {
            Calendar date = photos.get(0).getDateTaken();
            String dateStr = (date.get(Calendar.MONTH) + 1) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + date.get(Calendar.YEAR);
            return dateStr;
        } else 
        {
            Calendar earliest = getEarliestDate();
            Calendar latest = getLatestDate();
            String earliestStr = (earliest.get(Calendar.MONTH) + 1) + "/" + earliest.get(Calendar.DAY_OF_MONTH) + "/" + earliest.get(Calendar.YEAR);
            String latestStr = (latest.get(Calendar.MONTH) + 1) + "/" + latest.get(Calendar.DAY_OF_MONTH) + "/" + latest.get(Calendar.YEAR);
            return earliestStr + " - " + latestStr;
        }
    }

    /**
     * Updates the album's date range based on the current photos.
     */
    public void updateDateRange(){ this.dateRange = getDateRange(); }

    /**
     * Adds a photo to the album. Throws an exception if the photo is null or already exists in the album.
     * 
     * @param photo the photo to add to the album
     */
    public void addPhoto(Photo photo) {
        
        if (photo == null) 
        {
            throw new IllegalArgumentException("Photo cannot be null");
        }
        
        if (photos.contains(photo))
        {
            throw new IllegalArgumentException("Photo already exists in album.");
        }
        updateDateRange();
        photos.add(photo);
    }

    /**
     * Sets the name of the album.
     * 
     * @param name the new name of the album
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the album, which is its name.
     * 
     * @return the name of the album
     */
    public String toString() {
        return name;
    }

    /**
     * Removes a photo from the album. Throws an exception if the photo is null or not found in the album.
     * 
     * @param photo the photo to remove from the album
     */
    public void removePhoto(Photo photo) {
        
        if (photo == null) 
        {
            throw new IllegalArgumentException("Photo cannot be null");
        }
        
        if (!photos.contains(photo)) 
        {
            throw new IllegalArgumentException("Photo not found in the album");
        }
        
        updateDateRange();
        photos.remove(photo);
    }

    /**
     * Returns the thumbnail of the album, which is the first photo, or null if the album is empty.
     * 
     * @return the thumbnail photo or null
     */
    public Photo getThumbnail() {
        return photos.isEmpty() ? null : photos.get(0); 
    }

    /**
     * Returns a new list containing all the photos in the album.
     * 
     * @return a list of all photos in the album
     */
    public List<Photo> getPhotos() {
        return new ArrayList<Photo>(photos);
    }

    /**
     * Returns the name of the album.
     * 
     * @return the name of the album
     */
    public String getName() {
        return name;
    }

    /**
     * Renames the album to the specified new name.
     * Throws an exception if the new name is null or empty.
     * 
     * @param newName the new name for the album
     */
    public void rename(String newName) {
        
        if (newName == null || newName.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Album name cannot be null or empty");
        }
        
        this.name = newName;
    }

    /**
     * Filters the photos in the album based on the provided predicate.
     * Returns a new album containing only the photos that match the predicate.
     * 
     * @param predicate the filter condition to apply to the photos
     * @return a new album containing the filtered photos
     */
    public Album filter(Predicate<Photo> predicate) {
        
        if (predicate == null) 
        {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        Album filteredAlbum = new Album(this.name);
        
        for (Photo photo : photos) 
            if (predicate.test(photo)) filteredAlbum.addPhoto(photo); // Add to the new filtered album if it matches the predicate
            
        return filteredAlbum; // Return the new album containing only the photos that match the predicate
    }

    /**
     * Adds a caption to the specified photo. Throws an exception if the caption or photo is null or if the photo is not found in the album.
     * 
     * @param photo the photo to caption
     * @param caption the caption to add to the photo
     */
    public void captionPhoto(Photo photo, String caption){
        
        if (caption == null) 
        {
            throw new IllegalArgumentException("Caption cannot be null");
        }
        
        if (photo == null) 
        {
            throw new IllegalArgumentException("Photo cannot be null");
        }
        
        if (!photos.contains(photo)) 
        {
            throw new IllegalArgumentException("Photo not found in the album");
        }
        
        photo.setCaption(caption); // Assuming Photo class has a method caption(String caption) to set the caption
    }

    /**
     * Moves a photo from the current album to the specified target album.
     * Throws an exception if the photo or target album is null, or if the photo is not found in the current album.
     * 
     * @param photo the photo to move
     * @param targetAlbum the album to move the photo to
     */
    public void movePhoto(Photo photo, Album targetAlbum) {
        
        if (photo == null) 
        {
            throw new IllegalArgumentException("Photo cannot be null");
        }
        
        if (targetAlbum == null) 
        {
            throw new IllegalArgumentException("Target album cannot be null");
        }
        
        if (!this.photos.contains(photo)) 
        {
            throw new IllegalArgumentException("Photo not found in the current album");
        }
        
        this.removePhoto(photo); // Remove from the current album
        targetAlbum.addPhoto(photo); // Add to the target album
    }
    
    /**
     * Returns the photo at the current index in the album.
     * Throws an exception if the index is out of bounds.
     * 
     * @return the photo at the current index
     */
    public Photo getPhoto() {
        
        if (index < 0 || index >= photos.size()) 
        {
            throw new IndexOutOfBoundsException("Index out of bounds for the photos list");
        }
        
        return photos.get(index); // Return the photo at the specified index
    }

    /**
     * Moves to the next photo in the album. If the end of the album is reached, it wraps around to the first photo.
     */
    public void nextPhoto() {
        
        if (photos.isEmpty())
        {
            throw new IllegalStateException("No photos in the album to navigate");
        }
        
        index++; // Move to the next photo
        
        if (index >= photos.size()) 
        { // Wrap around if it exceeds the size of the list
            index = 0;
        }
    }

    /**
     * Moves to the previous photo in the album. If the beginning of the album is reached, it wraps around to the last photo.
     */
    public void previousPhoto() {
        if (photos.isEmpty()) 
        {
            throw new IllegalStateException("No photos in the album to navigate");
        }
        
        index--; // Move to the previous photo
        
        if (index < 0) 
        { // Wrap around to the last photo if it goes below 0
            index = photos.size() - 1;
        }
    }

    /**
     * Returns the earliest date of all photos in the album.
     * If the album is empty, returns null.
     * 
     * @return the earliest date of a photo in the album
     */
    public Calendar getEarliestDate() {
        
        if (photos.isEmpty()) 
        {
            return null;
        }
        
        Calendar earliest = photos.get(0).getDateTaken();
        for (Photo photo : photos) 
        {
            Calendar date = photo.getDateTaken();
            
            if (date.before(earliest)) 
            {
                earliest = date;
            }
        }

        return earliest;
    }

    /**
     * Returns the latest date of all photos in the album.
     * If the album is empty, returns null.
     * 
     * @return the latest date of a photo in the album
     */
    public Calendar getLatestDate() {
        
        if (photos.isEmpty()) 
        {
            return null;
        }
        
        Calendar latest = photos.get(0).getDateTaken();
        for (Photo photo : photos) 
        {
            Calendar date = photo.getDateTaken();
            
            if (latest.before(date)) 
            {
                latest = date;
            }
        }

        return latest;
    }
}
