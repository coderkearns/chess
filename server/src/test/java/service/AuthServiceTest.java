package service;

import dataaccess.MemoryAuthDAO;
import exceptions.Exceptions;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {
    AuthService service;
    MemoryAuthDAO dao;
    AuthData existingAuth = new AuthData("token", "username");
    AuthData nonexistentAuth = new AuthData("fake-token", "fake-username");

    @BeforeEach
    void setUp() {
        dao = new MemoryAuthDAO();
        dao.add(existingAuth);
        service = new AuthService(dao);
    }

    @Test
    void authDAO() {
        assertEquals(1, dao.records.size());
        assertFalse(dao.add(existingAuth));
        assertTrue(dao.delete(existingAuth));
        assertEquals(0, dao.records.size());
        assertTrue(dao.add(existingAuth));
        dao.add(nonexistentAuth);
        assertEquals(2, dao.records.size());
        assertEquals(nonexistentAuth.username(), dao.getUsername(nonexistentAuth.authToken()));
        dao.clear();
        assertEquals(0, dao.records.size());
    }

    @Test
    void verify() {
        assertEquals(existingAuth.authToken(), service.verify(existingAuth.authToken()));
        assertThrows(Exceptions.NotAuthorizedException.class, () -> {
            service.verify(nonexistentAuth.authToken());
        });
    }

    @Test
    void deleteAuth() {
        service.deleteAuth(existingAuth.authToken());
        assertEquals(0, dao.records.size());
    }

    @Test
    void clear() {
        dao.add(nonexistentAuth);
        service.clear();
        assertEquals(0, dao.records.size());
    }
}