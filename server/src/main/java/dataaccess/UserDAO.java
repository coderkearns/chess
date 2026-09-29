package dataaccess;

import model.UserData;

public interface UserDAO {
    /**
     * Stores a new user in the data layer. Returns true if they were added successfully, or false if they already exist.
     */
    boolean add(UserData user);

    /**
     * Checks if a user is stored in the data layer.
     */
    boolean exists(UserData user);

    /**
     * Finds a user with a specified username, or returns null if none exists.
     */
    public UserData find(String username);

    /**
     * Wipes all users from the data layer.
     */
    void clear();
}
