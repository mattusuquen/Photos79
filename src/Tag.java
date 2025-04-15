package src;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a tag with a name and a value. This class is used to store 
 * key-value pairs, where the name is a string representing the tag's name,
 * and the value is a string representing the tag's value.
 * <p>
 * Implements {@link Serializable} to allow serialization of Tag objects.
 * </p>
 */
public class Tag implements Serializable {
    
    /** The serial version UID for this class. */
    private static final long serialVersionUID = 1L;
    
    /** The name of the tag. */
    private String name;
    
    /** The value of the tag. */
    private String value;

    /**
     * Constructs a new {@code Tag} with the specified name and value.
     *
     * @param name the name of the tag
     * @param value the value associated with the tag
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name of the tag
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the value associated with the tag.
     *
     * @return the value of the tag
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of the tag in the format "name : value".
     *
     * @return a string representing the tag
     */
    @Override
    public String toString() {
        return name + " : " + value;
    }

    /**
     * Compares this tag with the specified object for equality.
     * Two tags are considered equal if their names and values are equal,
     * ignoring case.
     *
     * @param obj the object to compare this tag with
     * @return {@code true} if the object is a tag and is equal to this tag,
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        Tag other = (Tag) obj;
        return this.name.equalsIgnoreCase(other.name) && this.value.equalsIgnoreCase(other.value);
    }

    /**
     * Returns a hash code value for the tag. The hash code is computed based on
     * the name and value of the tag, both converted to lowercase.
     *
     * @return a hash code value for the tag
     */
    public int hash_Code() {
        return Objects.hash(name.toLowerCase(), value.toLowerCase());
    }

    /**
     * Returns a string representation of the tag in the format "name=value".
     *
     * @return a string representing the tag in "name=value" format
     */
    public String to_String() {
        return name + "=" + value;
    }
}
