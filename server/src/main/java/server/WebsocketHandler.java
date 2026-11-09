package server;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import service.AuthService;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebsocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections;

    final AuthService authService;
    final GameService gameService;
    final UserService userService;

    private final Gson gson;

    public WebsocketHandler(AuthService authService, GameService gameService, UserService userService) {
        this.authService = authService;
        this.gameService = gameService;
        this.userService = userService;
        gson = new Gson();
        connections = new ConnectionManager();
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            System.out.println("Got message: " + ctx.message());
            UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> onConnect(command, ctx);
                case LEAVE -> onLeave(command, ctx);
                case RESIGN -> onResign(command, ctx);
                case MAKE_MOVE -> onMakeMove(command, ctx);
            }
        } catch (IOException e) {
            System.err.println("error communicating with websocket");
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    // ----- Websocket Command Handlers -----
    private void onConnect(UserGameCommand command, WsMessageContext ctx) {
        connections.add(command.getGameID(), ctx.session);
    }

    private void onLeave(UserGameCommand command, WsMessageContext ctx) {
        connections.remove(command.getGameID(), ctx.session);
    }

    private void onResign(UserGameCommand command, WsMessageContext ctx) {
    }

    private void onMakeMove(UserGameCommand command, WsMessageContext ctx) throws IOException {
        connections.broadcast(command.getGameID(), null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION));
    }

}
