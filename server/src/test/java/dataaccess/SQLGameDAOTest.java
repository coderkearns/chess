package dataaccess;

import chess.ChessGame;
import model.GameData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

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

        var game = new GameData(-1, null, null, "my_game", new ChessGame());
        var addedGame = dao.add(game);

        assertTrue(addedGame.gameID() > 0);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void findSuccessfully(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

        var expectedGame = dao.add(new GameData(-1, null, null, "my_game", new ChessGame()););
        var actualGame = dao.find(expectedGame.gameID());

        assertEquals(expectedGame, actualGame);
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void findMissing(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

        var actualGame = dao.find(-1);

        assertNull(actualGame);
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