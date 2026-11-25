package client;

import chess.ChessGame;
import client.websocket.WebsocketManager;
import client.websocket.WebsocketSubscriber;
import exceptions.Exceptions;
import ui.ConsoleUI;
import ui.UI;
import websocket.messages.ServerMessage;

import java.util.Arrays;

public class Repl implements WebsocketSubscriber {
    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        PLAYING,
        OBSERVING,
        QUIT
    }

    public static class Context {
        public String authToken = null;
        public int gameID = -1;
        public ChessGame chessGame = null;
        public ChessGame.TeamColor playerColor = null;
    }

    private final UI ui;
    private final ServerFacade facade;
    private State state;
    private Context context;

    public Repl(String serverUrl) {
        ui = new ConsoleUI();
        var ws = new WebsocketManager(serverUrl, this);
        facade = new ServerFacade(serverUrl, ws);
    }

    public void run() {
        initializeState();

        ui.print("♕ Welcome to CS 240 Chess Client. Type Help for help. ♕");

        while (state != State.QUIT) {
            var commandParts = ui.getCommand(state.toString()).split(" ");
            var commandName = commandParts[0];
            var args = Arrays.copyOfRange(commandParts, 1, commandParts.length);
            var command = ReplCommands.commandMap.get(commandName);

            if (command == null) {
                command = new ReplCommands.InvalidCommand(commandName);
            }
            if (!command.getAllowedStates().contains(state)) {
                command = new ReplCommands.WrongStateCommand(commandName);
            }

            executeCommand(command, args);
        }
    }

    private void initializeState() {
        state = State.LOGGED_OUT;
        context = new Context();
    }

    private void executeCommand(ReplCommands.Command command, String[] args) {
        var ctx = new ReplCommands.CommandContext(ui, facade, state, context);
        try {
            state = command.execute(args, ctx);
        } catch (ReplCommands.ImproperUsageException e) {
            state = new ReplCommands.ImproperUsage(command, e).execute(args, ctx);
        } catch (Exceptions.ServerErrorException e) {
            ui.printError(e.getMessage());
        }
    }

    public void onWebsocketError(ServerMessage.Error msg) {
        ui.printError("\n\n" + msg.getMessage());
    }

    public void onWebsocketNotification(ServerMessage.Notification msg) {
        ui.print("\n" + msg.getMessage());
    }

    public void onWebsocketLoadGame(ServerMessage.LoadGame msg) {
        context.chessGame.setTeamTurn(msg.getGame().getTeamTurn());
        context.chessGame.setBoard(msg.getGame().getBoard());

        if (msg.getGame().isGameOver()) {
            context.chessGame.setGameOver(true);
            context.chessGame.setWinner(msg.getGame().getWinner());
            var winMessage = msg.getGame().getWinner() == null ? "Stalemate!" : String.format("%s won!", msg.getGame().getWinner());
            ui.print(String.format("\n%s\n[OBSERVING] >>> ", winMessage));

            state = State.OBSERVING;
        }
    }
}
