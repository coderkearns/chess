package server;

import com.google.gson.Gson;
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

    // Example handler
    // public void addPet(Context ctx) throws ResponseException {
    // Pet pet = new Gson().fromJson(ctx.body(), Pet.class);
    // pet = service.addPet(pet);
    // webSocketHandler.makeNoise(pet.name(), pet.sound());
    // ctx.result(new Gson().toJson(pet));
    // }

    public void postUser(Context ctx) {
        UserData user = gson.fromJson(ctx.body(), UserData.class);
        AuthData auth = userService.register(user);
        ctx.result(gson.toJson(auth));
    }

    public void postSession(Context ctx) {
        UserService.LoginRequest loginRequest = gson.fromJson(ctx.body(), UserService.LoginRequest.class);
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
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void postGame(Context ctx) {
        authService.verify(ctx.header("Authorization"));
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void putGame(Context ctx) {
        authService.verify(ctx.header("Authorization"));
        // TODO implement
        ctx.result("{\"success\":true}");
    }

    public void deleteDb(Context ctx) {
        authService.clear();
        gameService.clear();
        userService.clear();
        // Defaults to 200 OK with an empty body
        // This should return a 204, but the phase 3 spec specifies 200
    }
}
