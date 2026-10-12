package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SQLAuthDAOTest {


    private AuthDAO getDataAccess(Class<? extends AuthDAO> databaseClass) throws DataAccessException {
        AuthDAO db;
        if (databaseClass.equals(SQLAuthDAO.class)) {
            db = new SQLAuthDAO();
        } else {
            db = new MemoryAuthDAO();
        }
        db.clear();
        return db;
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLAuthDAO.class, MemoryAuthDAO.class})
    void add(Class<? extends AuthDAO> dbClass) throws DataAccessException {
        AuthDAO dao = getDataAccess(dbClass);

        AuthData auth = new AuthData("token", "username");
        boolean added = dao.add(auth);

        assertTrue(added);
    }

    @Test
    void tokenExists() {
    }

    @Test
    void getUsername() {
    }

    @Test
    void delete() {
    }

    @Test
    void clear() {
    }
}