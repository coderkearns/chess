package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.Exceptions;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        userDAO.add(existingUser);
        service = new UserService(userDAO, authDAO);
    }

    @Test
    void registerNewUser() {
        var newAuth = service.register(nonexistentUser);

        assertEquals(nonexistentUser, userDAO.find(nonexistentUser.username()));

        assertEquals(nonexistentUser.username(), newAuth.username());
        assertNotNull(newAuth.authToken());
        assertFalse(newAuth.authToken().isEmpty());
        assertTrue(authDAO.tokenExists(newAuth));
    }

    @Test
    void registerExistingUser() {
        assertThrows(Exceptions.AlreadyTakenException.class, () -> {
            service.register(existingUser);
        });
    }

    @Test
    void loginExistingUser() {
        var newAuth = service.login(new UserService.LoginRequest(existingUser.username(), existingUser.password()));

        assertEquals(existingUser.username(), newAuth.username());
        assertNotNull(newAuth.authToken());
        assertFalse(newAuth.authToken().isEmpty());
        assertTrue(authDAO.tokenExists(newAuth));
    }

    @Test
    void loginNonExistingUser() {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> {
            service.login(new UserService.LoginRequest(nonexistentUser.username(), nonexistentUser.password()));
        });
    }

    @Test
    void loginTwice() {
        var loginRequest = new UserService.LoginRequest(existingUser.username(), existingUser.password());
        var authOne = service.login(loginRequest);
        var authTwo = service.login(loginRequest);
        assertEquals(authOne.username(), authTwo.username());
        assertNotEquals(authOne.authToken(), authTwo.authToken());
    }

    @Test
    void clear() {
        userDAO.add(nonexistentUser);
        service.clear();
        assertEquals(0, userDAO.records.size());
    }
}