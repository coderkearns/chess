package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.Exceptions;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService service;
    MemoryAuthDAO authDAO;
    MemoryUserDAO userDAO;

    UserData existingUser = new UserData("john", "password", "john@example.com");
    AuthData existingUserAuth = new AuthData("token", "john");

    UserData nonexistentUser = new UserData("jane", "password", "jane@example.com");

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userDAO.add(new UserData(existingUser.username(), BCrypt.hashpw(existingUser.password(), BCrypt.gensalt()), existingUser.email()));
        service = new UserService(userDAO, authDAO);
    }

    @Test
    void registerNewUser() throws DataAccessException {
        var newAuth = service.register(nonexistentUser);

        var added = userDAO.find(nonexistentUser.username());
        assertEquals(nonexistentUser.username(), added.username());
        assertEquals(nonexistentUser.email(), added.email());
        assertTrue(BCrypt.checkpw(nonexistentUser.password(), added.password()), "Passwords should be hashed with bcrypt");

        assertEquals(nonexistentUser.username(), newAuth.username());
        assertNotNull(newAuth.authToken());
        assertFalse(newAuth.authToken().isEmpty());
        assertTrue(authDAO.tokenExists(newAuth));
    }

    @Test
    void registerExistingUser() throws DataAccessException {
        assertThrows(Exceptions.AlreadyTakenException.class, () -> {
            service.register(existingUser);
        });
    }

    @Test
    void loginExistingUser() throws DataAccessException {
        String encryptedPassword = BCrypt.hashpw("wow", BCrypt.gensalt());
        boolean doPasswordsMatch = BCrypt.checkpw("wow", encryptedPassword);

        assertTrue(doPasswordsMatch);

        var newAuth = service.login(new UserService.LoginRequest(existingUser.username(), existingUser.password()));

        assertEquals(existingUser.username(), newAuth.username());
        assertNotNull(newAuth.authToken());
        assertFalse(newAuth.authToken().isEmpty());
        assertTrue(authDAO.tokenExists(newAuth));
    }

    @Test
    void loginNonExistingUser() throws DataAccessException {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> {
            service.login(new UserService.LoginRequest(nonexistentUser.username(), nonexistentUser.password()));
        });
    }

    @Test
    void loginTwice() throws DataAccessException {
        var loginRequest = new UserService.LoginRequest(existingUser.username(), existingUser.password());
        var authOne = service.login(loginRequest);
        var authTwo = service.login(loginRequest);
        assertEquals(authOne.username(), authTwo.username());
        assertNotEquals(authOne.authToken(), authTwo.authToken());
    }

    @Test
    void clear() throws DataAccessException {
        userDAO.add(nonexistentUser);
        service.clear();
        assertEquals(0, userDAO.records.size());
    }
}