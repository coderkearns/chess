package client;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() {

    }

    @Test
    public void registerAlreadyExists() {

    }

    @Test
    public void login() {

    }

    @Test
    public void loginNotAuthorized() {

    }

    @Test
    public void logout() {

    }

    @Test
    public void logoutNotSignedIn() {

    }

    @Test
    public void listGames() {

    }

    @Test
    public void createGame() {

    }

    @Test
    public void createGameNotAuthorized() {

    }

    @Test
    public void joinGame() {

    }

    @Test
    public void joinGameNotAuthorized() {

    }

    @Test
    public void joinGameNotExist() {

    }

    @Test
    public void joinGameColorTaken() {

    }

    @Test
    public void clear() {
        assertDoesNotThrow(() -> facade.clear());
    }
}
