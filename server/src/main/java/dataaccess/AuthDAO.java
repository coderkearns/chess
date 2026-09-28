package dataaccess;

public interface AuthDAO {
    /**
     * Stores a new authToken in the data layer. Returns true if it was added successfully, or false if it already exists.
     */
    boolean add(String authToken);

    /**
     * Checks if an authToken is stored in the data layer.
     */
    boolean exists(String authToken);

    /**
     * Removes an authToken from the data layer. Returns true if it existed and was deleted, or false if it didn't exist.
     */
    boolean delete(String authToken);

    /**
     * Wipes all authTokens from the data layer.
     */
    void clear();
}
