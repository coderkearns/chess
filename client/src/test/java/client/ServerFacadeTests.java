package client;

import com.google.gson.Gson;
import exceptions.Exceptions;
import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Server;

import java.util.Collection;
import java.util.Map;

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
    public void register() {
        String authToken = facade.register("john", "wowcool", "john@example.com");

        assertTrue(authToken.length() > 1);
    }

    @Test
    public void registerAlreadyExists() {
        facade.register("john", "wowcool", "john@example.com");

        assertThrows(Exceptions.AlreadyTakenException.class, () -> facade.register("john", "wowcool", "john@example.com"));
    }

    @Test
    public void login() {
        facade.register("john", "wowcool", "john@example.com");
        String authToken = facade.login("john", "wowcool");

        assertTrue(authToken.length() > 1);
    }

    @Test
    public void loginNotAuthorized() {
        Map<String, Collection<UserData>> m = new Gson().fromJson("{\"games\": [{\"username\":\"john\",\"email\":\"email@example.com\",\"password\":\"cool\"}]}", Map.class);
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
