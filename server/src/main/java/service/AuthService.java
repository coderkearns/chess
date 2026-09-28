package service;

import dataaccess.AuthDAO;

public class AuthService {
    final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    /**
     * Delete all authTokens.
     */
    public void clear() {
        authDAO.clear();
    }
}
