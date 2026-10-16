package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.Exceptions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;
import ui.ConsoleUI;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
        facade.clear();
    }

    @BeforeEach
    public void beforeEach() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void test() {
        var ui = new ConsoleUI();
        var board = new ChessBoard();
        board.resetBoard();
        ui.showChessBoard(board, ChessGame.TeamColor.WHITE);
        System.out.println();
        ui.showChessBoard(board, ChessGame.TeamColor.BLACK);
    }

    // ----- HTTP & Functionality Tests -----
    @Test
    public void badServerURL() {
        ServerFacade badFacade = new ServerFacade("this is a bad url");

        assertThrows(Exceptions.ServerErrorException.class, badFacade::clear);
    }

    @Test
    public void noServerConnection() {
        ServerFacade badFacade = new ServerFacade("http://example.dev:9999");

        assertThrows(Exceptions.ServerErrorException.class, badFacade::clear);
    }

    // ----- Method Tests -----

    @Test
    public void register() {
        String authToken = facade.register("john", "mypassword", "john@example.com");

        assertTrue(authToken.length() > 1);
    }

    @Test
    public void registerAlreadyExists() {
        facade.register("john", "mypassword", "john@example.com");

        assertThrows(Exceptions.AlreadyTakenException.class, () -> facade.register("john", "mypassword", "john@example.com"));
    }

    @Test
    public void login() {
        facade.register("john", "mypassword", "john@example.com");
        String authToken = facade.login("john", "mypassword");

        assertTrue(authToken.length() > 1);
    }

    @Test
    public void loginNotAuthorized() {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.login("nonexistent", "nopassword"));

        facade.register("john", "mypassword", "john@example.com");

        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.login("john", "wrongpass"));
    }

    @Test
    public void logout() {
        String authToken = facade.register("john", "mypassword", "john@example.com");
        facade.logout(authToken);

        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.createGame(authToken, "myGame"));
    }

    @Test
    public void logoutNotSignedIn() {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.logout("fake-token"));
    }

    @Test
    public void listGames() {
        String authToken = facade.register("john", "mypassword", "john@example.com");
        int gameID1 = facade.createGame(authToken, "myGame");
        int gameID2 = facade.createGame(authToken, "myGame2");

        var games = facade.listGames(authToken);
        var expected = List.of(
                new ServerFacade.GameResponse(gameID1, null, null, "myGame"),
                new ServerFacade.GameResponse(gameID2, null, null, "myGame2")
        );

        assertEquals(expected, games);
    }

    @Test
    public void listGamesNotAuthorized() {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.listGames("fake-token"));
    }

    @Test
    public void createGame() {
        String authToken = facade.register("john", "mypassword", "john@example.com");

        int gameID = facade.createGame(authToken, "myGame");

        var actual = facade.listGames(authToken);
        var expected = List.of(
                new ServerFacade.GameResponse(gameID, null, null, "myGame")
        );

        assertTrue(gameID > 0);
        assertEquals(expected, actual);
    }

    @Test
    public void createGameNotAuthorized() {
        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.listGames("fake-token"));
    }

    @Test
    public void joinGame() {
        String authToken = facade.register("john", "mypassword", "john@example.com");
        int gameID = facade.createGame(authToken, "myGame");

        assertDoesNotThrow(() -> facade.joinGame(authToken, "WHITE", gameID));
    }

    @Test
    public void joinGameNotAuthorized() {
        String authToken = facade.register("john", "mypassword", "john@example.com");
        int gameID = facade.createGame(authToken, "myGame");

        assertThrows(Exceptions.NotAuthorizedException.class, () -> facade.joinGame("fake-token", "WHITE", gameID));
    }

    @Test
    public void joinGameNotExist() {
        String authToken = facade.register("john", "mypassword", "john@example.com");

        // I personally think this should throw a NotFoundException, but the professor's spec requires 400 not 404
        assertThrows(Exceptions.BadRequestException.class, () -> facade.joinGame(authToken, "WHITE", -1));
    }

    @Test
    public void joinGameColorTaken() {
        String authTokenJohn = facade.register("john", "mypassword", "john@example.com");
        String authTokenJane = facade.register("jane", "mypassword", "jane@example.com");

        int gameID = facade.createGame(authTokenJohn, "myGame");

        facade.joinGame(authTokenJohn, "WHITE", gameID);

        assertThrows(Exceptions.AlreadyTakenException.class, () -> facade.joinGame(authTokenJane, "WHITE", gameID));
    }

    @Test
    public void joinGameBadColor() {
        String authToken = facade.register("john", "mypassword", "john@example.com");
        int gameID = facade.createGame(authToken, "myGame");

        assertThrows(Exceptions.BadRequestException.class, () -> facade.joinGame(authToken, "RED", gameID));
    }

    @Test
    public void clear() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
