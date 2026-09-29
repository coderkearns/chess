package service;

import dataaccess.AuthDAO;
import exceptions.Exceptions.NotAuthorizedException;
import model.AuthData;

public class AuthService {
    final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    /**
     * Throws a NotAuthorizedException if the authToken is invalid. Returns the provided token otherwise.
     */
    public String verify(String authToken) {
        if (!authDAO.exists(new AuthData(authToken, null))) {
            throw new NotAuthorizedException();
        }
        return authToken;
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
