package server;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import exceptions.Exceptions;
import io.javalin.Javalin;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Server {

    private final Javalin javalin;

    public Server() {
        AuthDAO authDAO = null;
        GameDAO gameDAO = null;
        UserDAO userDAO = null;

        try {
            authDAO = new SQLAuthDAO();
            gameDAO = new SQLGameDAO();
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            System.err.println(e);
            System.err.println("Error accessing database during setup");
            javalin = null;
            return;
        }

        var authService = new AuthService(authDAO);
        var gameService = new GameService(authDAO, gameDAO);
        var userService = new UserService(userDAO, authDAO);

        var handlers = new Handlers(authService, gameService, userService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", handlers::postUser)
                .post("/session", handlers::postSession)
                .delete("/session", handlers::deleteSession)
                .get("/game", handlers::getGame)
                .post("/game", handlers::postGame)
                .put("/game", handlers::putGame)
                .delete("/db", handlers::deleteDb)
                .exception(Exceptions.BadRequestException.class, handlers::handleBadRequestException)
                .exception(Exceptions.NotAuthorizedException.class, handlers::handleNotAuthorizedException)
                .exception(Exceptions.AlreadyTakenException.class, handlers::handleAlreadyTakenException)
                .exception(Exceptions.NotFoundException.class, handlers::handleNotFoundException)
                .exception(DataAccessException.class, handlers::handleDataAccessException)
                .exception(RuntimeException.class, handlers::handleGenericException);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
