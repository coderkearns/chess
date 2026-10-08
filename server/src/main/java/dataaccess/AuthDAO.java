package dataaccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * Stores a new auth in the data layer. Returns true if it was added successfully, or false if it already exists.
     */
    boolean add(AuthData auth);
    
    /**
     * Checks if an auth is stored in the data layer.
     */
    boolean tokenExists(AuthData auth);

    /**
     * Finds a username attached to a given auth token. Returns null if it doesn't exist.
     */
    String getUsername(String authToken);

    /**
     * Removes an auth from the data layer. Returns true if it existed and was deleted, or false if it didn't exist.
     */
    boolean delete(AuthData auth);

    /**
     * Wipes all auths from the data layer.
     */
    void clear();
}
