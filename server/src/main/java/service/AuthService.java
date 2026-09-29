package service;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class AuthService {
    final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a new auth token for the given username, stores it in the data layer,
     * then returns the created auth data.
     */
    public AuthData createAuth(String username) {
        var auth = new AuthData(generateToken(), username);
        authDAO.add(auth);
        return auth;
    }

    /**
     * Returns true if the given authToken is valid in the data layer.
     */
    public boolean verify(String authToken) {
        return authDAO.exists(new AuthData(authToken, null));
    }

    /**
     * Deletes an auth token from the data layer. Returns true if it existed and was deleted.
     */
    public boolean deleteAuth(String authToken) {
        return authDAO.delete(new AuthData(authToken, null));
    }

    /**
     * Wipes all auth entries from the data layer.
     */
    public void clear() {
        authDAO.clear();
    }
}
