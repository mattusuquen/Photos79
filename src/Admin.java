package src;

import java.util.ArrayList;
import java.util.List;

public class Admin extends User {

    public Admin() {
        super("admin");
    }
    public void deleteUser(String username) {
        // Logic to delete a user from the system
        System.out.println("User " + username + " deleted by admin.");
    }
    public void createUser(String username) {
        // Logic to delete a user from the system
        System.out.println("User " + username + " deleted by admin.");
    }
    public List<User> listUsers() {
        // Logic to list all users in the system
        List<User> users = new ArrayList<User>();
        return users;
    }
}