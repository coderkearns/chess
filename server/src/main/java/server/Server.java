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
        var authService = new AuthService(new MemoryAuthDAO());
        var gameService = new GameService(new MemoryGameDAO());
        var userService = new UserService(new MemoryUserDAO());

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
