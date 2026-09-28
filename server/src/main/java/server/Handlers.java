package server;

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
}
