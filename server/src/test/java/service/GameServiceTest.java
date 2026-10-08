package service;

import chess.ChessGame;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import exceptions.Exceptions;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    MemoryAuthDAO authDAO;
    MemoryGameDAO gameDAO;
    GameService service;

    AuthData existingAuth = new AuthData("token", "username");
    GameData existingGame = new GameData(0, "whiteTaken", null, "initial game", new ChessGame());

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
        var gameResponse = (GameService.GameResponse) games.games().toArray()[0];
        assertEquals(existingGame.gameID(), gameResponse.gameID());
        assertEquals(existingGame.gameName(), gameResponse.gameName());
        assertEquals(existingGame.whiteUsername(), gameResponse.whiteUsername());
        assertEquals(existingGame.blackUsername(), gameResponse.blackUsername());
    }

    @Test
    void newGame() {
        var newGameResponse = service.newGame(new GameService.NewGameRequest("my new game"));
        assertEquals(1, newGameResponse.gameID());
        var gameData = gameDAO.find(newGameResponse.gameID());
        assertNotNull(gameData);
        assertEquals(newGameResponse.gameID(), gameData.gameID());
        assertEquals("my new game", gameData.gameName());

        var secondGameResponse = service.newGame(new GameService.NewGameRequest("game 2"));
        assertEquals(2, secondGameResponse.gameID());
    }

    @Test
    void joinGameEmptySpace() {
        service.joinGame(existingAuth.authToken(), new GameService.JoinGameRequest("BLACK", 0));
        var game = gameDAO.find(0);
        assertEquals(game.blackUsername(), existingAuth.username());
    }

    @Test
    void joinGameTaken() {
        assertThrows(Exceptions.AlreadyTakenException.class, () -> {
            service.joinGame(existingAuth.authToken(), new GameService.JoinGameRequest("WHITE", 0));
        });
    }

    @Test
    void joinNonExistentGame() {
        assertThrows(Exceptions.BadRequestException.class, () -> {
            service.joinGame(existingAuth.authToken(), new GameService.JoinGameRequest("WHITE", 1));
        });
    }

    @Test
    void clear() {
        gameDAO.add(new GameData(1, null, null, "name", null));
        service.clear();
        assertEquals(0, gameDAO.records.size());
    }
}