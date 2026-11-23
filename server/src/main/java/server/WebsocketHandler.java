package server;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exceptions.Exceptions;
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
                case MAKE_MOVE -> onMakeMove(gson.fromJson(ctx.message(), UserGameCommand.MakeMove.class), ctx);
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
    private void onConnect(UserGameCommand command, WsMessageContext ctx) throws IOException {
        connections.add(command.getGameID(), ctx.session);
        try {
            authService.verify(command.getAuthToken());
            var gameState = gameService.getCurrentState(command.getAuthToken(), command.getGameID());
            var playerDetails = gameService.getPlayerDetails(command.getAuthToken(), command.getGameID());
            ctx.session.getRemote().sendString(
                    new Gson().toJson(new ServerMessage.LoadGame(gameState))
            );
            var notification = playerDetails.color() == null
                    ? String.format("%s joined to observe!", playerDetails.username())
                    : String.format("%s joined as %s!", playerDetails.username(), playerDetails.color());
            connections.broadcast(command.getGameID(), ctx.session, new ServerMessage.Notification(notification));
        } catch (Exceptions.NotFoundException e) {
            sendError(ctx, "game not found");
        } catch (Exceptions.NotAuthorizedException e) {
            broadcastError(command.getGameID(), "not authorized");
        } catch (DataAccessException e) {
            broadcastError(command.getGameID(), "server error getting game data");
        }
    }

    private void onLeave(UserGameCommand command, WsMessageContext ctx) throws IOException {
        connections.remove(command.getGameID(), ctx.session);
    }

    private void onResign(UserGameCommand command, WsMessageContext ctx) throws IOException {
        try {
            authService.verify(command.getAuthToken());
            var gameState = gameService.resign(command.getAuthToken(), command.getGameID());
            connections.broadcast(command.getGameID(), null, new ServerMessage.LoadGame(gameState));
        } catch (Exceptions.NotAuthorizedException e) {
            broadcastError(command.getGameID(), "not authorized");
        } catch (DataAccessException e) {
            broadcastError(command.getGameID(), "server error getting game data");
        }
    }

    private void onMakeMove(UserGameCommand.MakeMove command, WsMessageContext ctx) throws IOException {
        try {
            authService.verify(command.getAuthToken());
            var gameState = gameService.makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
            connections.broadcast(command.getGameID(), null, new ServerMessage.LoadGame(gameState));
            connections.broadcast(command.getGameID(), ctx.session, new ServerMessage.Notification("Move made!"));
            if (gameState.isInCheck(gameState.getTeamTurn())) {
                connections.broadcast(command.getGameID(), null, new ServerMessage.Notification(String.format("%s is in check!", gameState.getTeamTurn())));
            }
        } catch (Exceptions.NotAuthorizedException e) {
            broadcastError(command.getGameID(), "not authorized");
        } catch (InvalidMoveException e) {
            broadcastError(command.getGameID(), e.getMessage());
        } catch (DataAccessException e) {
            broadcastError(command.getGameID(), "server error getting game data");
        }
    }

    // ----- Helpers -----
    private void sendError(WsMessageContext ctx, String msg) throws IOException {
        ctx.session.getRemote().sendString(new Gson().toJson(new ServerMessage.Error(msg)));
    }

    private void broadcastError(int gameID, String msg) throws IOException {
        connections.broadcast(gameID, null, new ServerMessage.Error(msg));
    }

}
