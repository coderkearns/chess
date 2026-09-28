package server;

import io.javalin.http.Context;
import service.AuthService;
import service.GameService;
import service.UserService;

public class Handlers {
    final AuthService authService;
    final GameService gameService;
    final UserService userService;

    public Handlers(AuthService authService, GameService gameService, UserService userService) {
        this.authService = authService;
        this.gameService = gameService;
        this.userService = userService;
    }

    // Example handler
    // public void addPet(Context ctx) throws ResponseException {
    // Pet pet = new Gson().fromJson(ctx.body(), Pet.class);
    // pet = service.addPet(pet);
    // webSocketHandler.makeNoise(pet.name(), pet.sound());
    // ctx.result(new Gson().toJson(pet));
    // }

    public void postUser(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void postSession(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void deleteSession(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void getGame(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void postGame(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void putGame(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void deleteDb(Context ctx) {
        // TODO implement
        ctx.result("{\"success\":true}");
    }
}
