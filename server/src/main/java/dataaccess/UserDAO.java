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
     * Removes a user from the data layer. Returns true if they existed and were deleted, or false if they didn't exist.
     */
    boolean delete(UserData user);

    /**
     * Wipes all users from the data layer.
     */
    void clear();
}
