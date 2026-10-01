package server;

import com.google.gson.Gson;
import exceptions.Exceptions;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handlers {
    final AuthService authService;
    final GameService gameService;
    final UserService userService;
    final Gson gson;

    public Handlers(AuthService authService, GameService gameService, UserService userService) {
        this.authService = authService;
        this.gameService = gameService;
        this.userService = userService;
        gson = new Gson();
    }

    /* --- HELPERS --- */

    /**
     * Throws a BadRequestException if provided any nulls
     */
    private void validateNotNull(Object... params) {
        for (Object param : params) {
            if (param == null) {
                throw new Exceptions.BadRequestException();
            }
        }
    }

    /* --- ROUTES --- */
    public void postUser(Context ctx) {
        UserData user = gson.fromJson(ctx.body(), UserData.class);
        validateNotNull(user.username(), user.password(), user.email());
        AuthData auth = userService.register(user);
        ctx.result(gson.toJson(auth));
    }

    public void postSession(Context ctx) {
        UserService.LoginRequest loginRequest = gson.fromJson(ctx.body(), UserService.LoginRequest.class);
        validateNotNull(loginRequest.username(), loginRequest.password());
        AuthData auth = userService.login(loginRequest);
        ctx.result(gson.toJson(auth));
    }

    public void deleteSession(Context ctx) {
        String authToken = authService.verify(ctx.header("Authorization"));
        authService.deleteAuth(authToken);
        // Defaults to 200 OK with an empty body
        // This should return a 204, but the phase 3 spec specifies 200
    }

    public void getGame(Context ctx) {
        authService.verify(ctx.header("Authorization"));
        var games = gameService.listGames();
        ctx.result(gson.toJson(games));
    }

    public void postGame(Context ctx) {
        authService.verify(ctx.header("Authorization"));
        GameService.NewGameRequest newGameRequest = gson.fromJson(ctx.body(), GameService.NewGameRequest.class);
        validateNotNull(newGameRequest.gameName());
        var newGameResponse = gameService.newGame(newGameRequest);
        ctx.result(gson.toJson(newGameResponse));
    }

    public void putGame(Context ctx) {
        var authToken = authService.verify(ctx.header("Authorization"));
        GameService.JoinGameRequest joinGameRequest = gson.fromJson(ctx.body(), GameService.JoinGameRequest.class);
        validateNotNull(joinGameRequest.gameID(), joinGameRequest.playerColor());
        gameService.joinGame(authToken, joinGameRequest);
        // Defaults to 200 OK with an empty body
        // This should return a 204, but the phase 3 spec specifies 200
    }

    public void deleteDb(Context ctx) {
        authService.clear();
        gameService.clear();
        userService.clear();
        // Defaults to 200 OK with an empty body
        // This should return a 204, but the phase 3 spec specifies 200
    }

    /* --- EXCEPTIONS --- */
    public void handleBadRequestException(Exceptions.BadRequestException e, Context ctx) {
        ctx.result("{\"message\": \"Error: bad request\"}").status(400);
    }

    public void handleNotAuthorizedException(Exceptions.NotAuthorizedException e, Context ctx) {
        ctx.result("{\"message\": \"Error: not authorized\"}").status(401);
    }

    public void handleAlreadyTakenException(Exceptions.AlreadyTakenException e, Context ctx) {
        ctx.result("{\"message\": \"Error: already taken\"}").status(403);
    }

    public void handleNotFoundException(Exceptions.NotFoundException e, Context ctx) {
        ctx.result("{\"message\": \"Error: not found\"}").status(404);
    }

    public void handleGenericException(RuntimeException e, Context ctx) {
        ctx.result("{\"message\": \"Error: internal server error\"}").status(500);
    }
}
