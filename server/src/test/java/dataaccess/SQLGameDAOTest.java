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

        var chessGame = new ChessGame();
        chessGame.setTeamTurn(ChessGame.TeamColor.BLACK);
        var expectedGame = dao.add(new GameData(-1, null, null, "my_game", chessGame));
        var actualGame = dao.find(expectedGame.gameID());

        assertEquals(expectedGame, actualGame);
        assertEquals(ChessGame.TeamColor.BLACK, actualGame.game().getTeamTurn(), "game json not encoded properly");
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

        var chessGame = new ChessGame();
        var insertedGame = dao.add(new GameData(-1, null, null, "my_game", chessGame));
        chessGame.setTeamTurn(ChessGame.TeamColor.BLACK);

        var newGame = new GameData(insertedGame.gameID(), "john_doe", null, "my_game", chessGame);
        dao.setGame(newGame);

        var persistedGame = dao.find(insertedGame.gameID());

        assertEquals(ChessGame.TeamColor.BLACK, persistedGame.game().getTeamTurn());
        assertEquals("john_doe", persistedGame.whiteUsername());
        assertNull(persistedGame.blackUsername());
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void setGameMissing(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

        var nonexistentGame = new GameData(-1, "john_doe", null, "my_game", new ChessGame());

        assertThrows(DataAccessException.class, () -> dao.setGame(nonexistentGame));
    }


    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void getAllEmpty(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

        var games = dao.getAll();

        assertEquals(0, games.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {SQLGameDAO.class, MemoryGameDAO.class})
    void getAll(Class<? extends GameDAO> daoClass) throws DataAccessException {
        GameDAO dao = getDataAccess(daoClass);

        dao.add(new GameData(-1, null, null, "game_1", new ChessGame()));
        dao.add(new GameData(-1, null, null, "game_2", new ChessGame()));

        var games = dao.getAll();

        assertEquals(2, games.size());
        assertEquals(6, ((GameData) games.toArray()[0]).gameName().length());
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

        dao.add(new GameData(-1, null, null, "game_1", new ChessGame()));
        dao.add(new GameData(-1, null, null, "game_2", new ChessGame()));

        assertDoesNotThrow(dao::clear);
        assertEquals(0, dao.getAll().size());
    }
}