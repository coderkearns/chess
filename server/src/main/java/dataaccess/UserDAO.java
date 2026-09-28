package dataaccess;

public interface UserDAO {
    public static record UserData(String username, String password, String email) {
    }

    /**
     * Stores a new user in the data layer. Returns true if they were added successfully, or false if they already exist.
     */
    boolean add(UserData user);

    /**
     * Checks if a user is stored in the data layer.
     */
    boolean exists(UserData user);

    /**
     * Removes a user from the data layer. Returns true if it existed and was deleted, or false if it didn't exist.
     */
    boolean delete(UserData user);

    /**
     * Wipes all users from the data layer.
     */
    void clear();
}
