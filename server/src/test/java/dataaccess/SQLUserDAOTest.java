package dataaccess;

import dataaccess.sql.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    private UserDAO getDataAccess(Class<? extends UserDAO> databaseClass) throws DataAccessException {
        UserDAO dao;
        if (databaseClass.equals(SQLUserDAO.class)) {
            dao = new SQLUserDAO();
        } else {
            dao = new MemoryUserDAO();
        }
        dao.clear();
        return dao;
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLUserDAO.class, MemoryUserDAO.class})
    void addSuccessfully(Class<? extends UserDAO> daoClass) throws DataAccessException {
        UserDAO dao = getDataAccess(daoClass);

        UserData user = new UserData("john_doe", "encrypted-password", "john@doe.example.com");
        boolean wasAdded = dao.add(user);

        assertTrue(wasAdded);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLUserDAO.class, MemoryUserDAO.class})
    void addAlreadyExists(Class<? extends UserDAO> daoClass) throws DataAccessException {
        UserDAO dao = getDataAccess(daoClass);

        UserData user1 = new UserData("john_doe", "encrypted-password", "john@doe.example.com");
        UserData user2 = new UserData("john_doe", "new-password", "new-email@doe.example.com");
        dao.add(user1);
        boolean wasAdded = dao.add(user2);

        assertFalse(wasAdded);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLUserDAO.class, MemoryUserDAO.class})
    void findSuccessfully(Class<? extends UserDAO> daoClass) throws DataAccessException {
        UserDAO dao = getDataAccess(daoClass);

        UserData expectedUser = new UserData("john_doe", "encrypted-password", "john@doe.example.com");
        dao.add(expectedUser);

        var actualUser = dao.find(expectedUser.username());

        assertEquals(expectedUser, actualUser);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLUserDAO.class, MemoryUserDAO.class})
    void findMissing(Class<? extends UserDAO> daoClass) throws DataAccessException {
        UserDAO dao = getDataAccess(daoClass);

        var actualUser = dao.find("i_dont_exist_user");

        assertNull(actualUser);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLUserDAO.class, MemoryUserDAO.class})
    void clear(Class<? extends UserDAO> daoClass) throws DataAccessException {
        UserDAO dao = getDataAccess(daoClass);

        dao.add(new UserData("john_doe", "encrypted-password", "john@doe.example.com"));
        dao.add(new UserData("jane_doe", "encrypted-password", "jane@doe.example.com"));

        assertDoesNotThrow(dao::clear);
    }
}