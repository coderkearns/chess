package dataaccess;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SQLGameDAOTest {
    private GameDAO getDataAccess(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao;
        if (daoClass.equals(SQLGameDAO.class)) {
            dao = new SQLGameDAO();
        } else {
            dao = new MemoryGameDAO();
        }
        dao.clear();
        return dao;
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void add(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void findSuccessfully(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void findMissing(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void setGame(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void setGameMissing(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }


    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void getAllEmpty(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void getAll(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void deleteSuccessfully(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void deleteMissing(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void clear(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

    }
}