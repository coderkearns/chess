package server;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import io.javalin.Javalin;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        var authDAO = new MemoryAuthDAO();
        var gameDAO = new MemoryGameDAO();
        var userDAO = new MemoryUserDAO();
        
        var authService = new AuthService(authDAO);
        var gameService = new GameService(gameDAO);
        var userService = new UserService(userDAO, authDAO);

        var handlers = new Handlers(authService, gameService, userService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", handlers::postUser)
                .post("/session", handlers::postSession)
                .delete("/session", handlers::deleteSession)
                .get("/game", handlers::getGame)
                .post("/game", handlers::postGame)
                .put("/game", handlers::putGame)
                .delete("/db", handlers::deleteDb);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
