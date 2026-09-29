package service;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exceptions.Exceptions.AlreadyTakenException;
import exceptions.Exceptions.NotAuthorizedException;
import model.AuthData;
import model.UserData;

import java.util.Objects;
import java.util.UUID;

public class UserService {
    final UserDAO userDAO;
    final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    /*--- Helpers ---*/

    /**
     * Generate a random auth record for a given user
     */
    private AuthData generateAuth(UserData user) {
        return new AuthData(UUID.randomUUID().toString(), user.username());
    }

    /* --- DTOs --- */

    public static record LoginRequest(String username, String password) {
    }
    
    /* --- Methods --- */

    public AuthData register(UserData req) {
        var existingUser = userDAO.find(req.username());
        if (existingUser != null) {
            throw new AlreadyTakenException();
        }
        userDAO.add(req);
        var auth = generateAuth(req);
        authDAO.add(auth);
        return auth;
    }

    public AuthData login(LoginRequest req) {
        var existingUser = userDAO.find(req.username());
        if (existingUser == null || !Objects.equals(req.password(), existingUser.password())) {
            throw new NotAuthorizedException();
        }
        var auth = generateAuth(existingUser);
        authDAO.add(auth);
        return auth;
    }


    public void clear() {
        userDAO.clear();
    }
}
