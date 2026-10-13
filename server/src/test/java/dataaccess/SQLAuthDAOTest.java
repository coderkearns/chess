package dataaccess;

import model.AuthData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {


    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        AuthDAO dao;
        if (databaseClass.equals(SQLAuthDAO.class)) {
            dao = new SQLAuthDAO();
        } else {
            dao = new MemoryAuthDAO();
        }
        dao.clear();
        return dao;
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void add(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        boolean added = dao.add(auth);

        assertTrue(added);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void addAlreadyExists(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        dao.add(new AuthData("token", "username"));
        boolean added = dao.add(new AuthData("token", "new-username"));

        assertFalse(added);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void tokenExists(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        dao.add(auth);

        assertTrue(dao.tokenExists(auth));
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void tokenExistsMissing(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        assertFalse(dao.tokenExists(new AuthData("nonexistent-token", null)));
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void getUsername(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        dao.add(auth);

        var actualUsername = dao.getUsername("token");

        assertEquals("username", actualUsername);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void getUsernameMissing(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        var actualUsername = dao.getUsername("nonexistent-token");

        assertNull(actualUsername);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void delete(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        dao.add(auth);

        boolean deleted = dao.delete(auth);

        assertTrue(deleted);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void deleteNonExistent(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        boolean deleted = dao.delete(auth);

        assertFalse(deleted);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void clear(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        dao.add(new AuthData("token1", "username1"));
        dao.add(new AuthData("token2", "username2"));

        assertDoesNotThrow(dao::clear);
    }
}