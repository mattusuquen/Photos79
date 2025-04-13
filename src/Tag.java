package src;

import java.io.Serializable;
import java.util.Objects;
public class Tag implements  Serializable{
    
    private static final long serialVersionUID = 1L;
    private String name;
    private String value;

    public Tag (String name, String value) {
        this.name = name;
        this.value = value;
    }



    public String getName() 
    {
        return this.name;
    }
    
    
    public String getValue() 
    {
        return this.value;
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) 
        {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) 
        {
            return false;
        }
        
        Tag other = (Tag) obj;
        return this.name.equalsIgnoreCase(other.name) && this.value.equalsIgnoreCase(other.value);
    }

    
    public int hash_Code() 
    {
        return Objects.hash(name.toLowerCase(), value.toLowerCase());
    }
    

    public String to_String() 
    {
        return name + "=" + value;
    }


    
}