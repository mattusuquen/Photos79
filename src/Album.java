package src;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class Album implements Serializable {
    
    private static final long serialVersionUID = 1L; 
    private String name;
    private List<Photo> photos;
    private int index = 0;
    private String dateRange = "HI"; 
    
    
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
        this.dateRange = "N/A";
    }

    public int getPhotoCount() {
        return photos.size();
    }

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
    public void updateDateRange(){ this.dateRange = getDateRange(); }
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

    public void setName(String name) {
        this.name = name;
    }
    public String toString() {
        return name;
    }
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

    public Photo getThumbnail() {
        return photos.isEmpty() ? null : photos.get(0); 
    }

    public List<Photo> getPhotos() {
        return new ArrayList<Photo>(photos);
    }
    
    
    
    public String getName() {
        return name;
    }
    
    
    
    public void rename(String newName) {
        
        if (newName == null || newName.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Album name cannot be null or empty");
        }
        
        this.name = newName;
    }
    
    
    
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
    
    public Photo getPhoto() {
        
        if (index < 0 || index >= photos.size()) 
        {
            throw new IndexOutOfBoundsException("Index out of bounds for the photos list");
        }
        
        return photos.get(index); // Return the photo at the specified index
    }


    
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