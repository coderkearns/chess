package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.Exceptions;
import ui.ConsoleUI;
import ui.UI;

import java.util.Arrays;

public class Repl {
    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        PLAYING,
        OBSERVING,
        QUIT
    }

    public static class Context {
        public String authToken = null;
        public ChessBoard chessBoard = null;
        public ChessGame.TeamColor playerColor = null;
    }

    private final UI ui;
    private final ServerFacade facade;
    private State state;
    private Context context;

    public Repl(String serverUrl) {
        ui = new ConsoleUI();
        facade = new ServerFacade(serverUrl);
    }

    public void run() {
        initializeState();

        ui.print("♕ Welcome to CS 240 Chess Client. Type Help for help. ♕");

        while (state != State.QUIT) {
            if (state == State.PLAYING || state == State.OBSERVING) {
                ui.showChessBoard(context.chessBoard, context.playerColor);
            }

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
}
