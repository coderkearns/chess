package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceTest {
    MemoryAuthDAO authDAO;
    MemoryGameDAO gameDAO;
    GameService service;

    AuthData existingAuth = new AuthData("token", "username");
    GameData existingGame = new GameData(0, "white", "black", "initial game", new ChessGame());

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        authDAO.add(existingAuth);
        gameDAO = new MemoryGameDAO();
        gameDAO.add(existingGame);
        service = new GameService(authDAO, gameDAO);
    }

    @Test
    void listGames() {
        var games = service.listGames();
        assertEquals(1, games.games().size());
        assertEquals(games.games().toArray()[0], existingGame);
    }

    @Test
    void newGame() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void clear() {
    }
}