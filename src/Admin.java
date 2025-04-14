package src;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Admin extends User {
    private Map<String, User> users;

    public Admin() {
        super("admin");
        this.users = DataManager.loadAllUsers(); 
    }
    public void deleteUser(String username) {
        // Logic to delete a user from the system
        if(username.equalsIgnoreCase("admin"))
        {
            System.out.println("Cant Delete admin.");
            return; 
        }
        User user = users.remove(username);
        
        if(user != null)
        {
            File file = new File(DataManager. + username + ".dat");
            file.delete(); 
            System.out.println("User " + username + " deleted by admin.");
        }
        else
        {
            System.out.println("User " + username + "Not Found.");
        }
        
    }
    
    
    
    public void createUser(String username) {
        
        if (users.containsKey(username)) 
        {
            System.out.println("User " + username + " already exists.");
            return;
        }
        
        User newUser = new User(username);
        users.put(username, newUser);
        
        try 
        {
            DataManager.saveUser(newUser);
            System.out.println("User " + username + " created by admin.");
        } 
        
        catch (IOException e) 
        {
            System.out.println("Failed to create user: " + e.getMessage());
        }
    }
    
    
    
    public List<User> listUsers() {
        // Logic to list all users in the system
        List<User> users = new ArrayList<User>();
        return users;
    }
}