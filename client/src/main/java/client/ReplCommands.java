package client;

import chess.ChessBoard;
import chess.ChessGame;
import exceptions.Exceptions;
import ui.UI;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReplCommands {
    public static record CommandContext(UI ui, ServerFacade facade, Repl.State state, Repl.Context context) {
    }

    public static class ImproperUsageException extends RuntimeException {
        ImproperUsageException(String message) {
            super(message);
        }
    }

    public static abstract class Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN, Repl.State.LOGGED_OUT, Repl.State.PLAYING, Repl.State.OBSERVING);
        }

        public abstract Repl.State execute(String[] args, CommandContext ctx);

        protected void validateArgs(String[] args, int expectedLength) {
            if (args.length != expectedLength) {
                throw new ImproperUsageException("wrong number of arguments");
            }
        }
    }

    public static Map<String, Command> commandMap = Map.of(
            "quit", new Quit(),
            "help", new Help(),
            "register", new Register(),
            "login", new Login(),
            "logout", new Logout(),
            "create", new CreateGame(),
            "list", new ListGames(),
            "join", new JoinGame(),
            "observe", new ObserveGame()
    );

    // ----- COMMANDS -----

    private static class Quit extends Command {
        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print("Bye!");
            return Repl.State.QUIT;
        }
    }

    private static class Help extends Command {
        public Repl.State execute(String[] args, CommandContext ctx) {
            var helpText = switch (ctx.state) {
                case LOGGED_IN -> """
                        \tcreate <NAME> - start a new game
                        \tlist - all current games and their details
                        \tjoin <ID> <WHITE|BLACK> - join an existing game as the specified color
                        \tobserve <ID> - watch a game without joining
                        \tlogout - log out of the current user session
                        \tquit - end the program
                        \thelp - show possible commands""";
                case LOGGED_OUT -> """
                        \tregister <USERNAME> <PASSWORD> <EMAIL> - create an account and log in
                        \tlogin <USERNAME> <PASSWORD> - to an existing account
                        \tquit - end the program
                        \thelp - show possible commands""";
                default -> """
                        \tquit - end the program
                        \thelp - show possible commands""";
            };
            ctx.ui.print(helpText);
            return ctx.state;
        }
    }

    private static class Register extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_OUT);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 3);
            ctx.ui.print("Registering...");
            ctx.context.authToken = ctx.facade.register(args[0], args[1], args[2]);
            ctx.ui.print(String.format("Account created and logged in as %s!", args[0]));
            return Repl.State.LOGGED_IN;
        }
    }

    private static class Login extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_OUT);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 2);
            ctx.ui.print("Logging in...");
            ctx.context.authToken = ctx.facade.login(args[0], args[1]);
            ctx.ui.print(String.format("Logged in as %s!", args[0]));
            return Repl.State.LOGGED_IN;
        }
    }

    private static class Logout extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print("Logging out...");
            ctx.facade.logout(ctx.context.authToken);
            ctx.context.authToken = null;
            ctx.ui.print("Logged out!");
            return Repl.State.LOGGED_OUT;
        }
    }

    private static class CreateGame extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 1);
            int gameID = ctx.facade.createGame(ctx.context.authToken, args[0]);
            ctx.ui.print(String.format("Created game %s with ID %d!", args[0], gameID));
            return ctx.state;
        }
    }

    private static class ListGames extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            var games = ctx.facade.listGames(ctx.context.authToken);
            ctx.ui.printGameList(games);
            return ctx.state;
        }
    }

    private static class JoinGame extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 2);

            int gameID;
            try {
                gameID = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new ImproperUsageException("gameID must be an integer");
            }

            if (!Objects.equals(args[1], "WHITE") && !Objects.equals(args[1], "BLACK")) {
                throw new ImproperUsageException("color must be exactly 'WHITE' or 'BLACK'");
            }


            ctx.facade.joinGame(ctx.context.authToken, args[1], gameID);

            // TODO this will be handled by websocket later
            ctx.context.chessBoard = new ChessBoard();
            ctx.context.chessBoard.resetBoard();
            ctx.context.playerColor = Objects.equals(args[1], "WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;

            return Repl.State.PLAYING;
        }
    }

    private static class ObserveGame extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 1);

            int gameID;
            try {
                gameID = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                throw new ImproperUsageException("gameID must be an integer");
            }

            var games = ctx.facade.listGames(ctx.context.authToken);

            boolean found = false;
            for (var game : games) {
                if (game.gameID() == gameID) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new Exceptions.NotFoundException();
            }

            // TODO this will be handled by websocket later
            ctx.context.chessBoard = new ChessBoard();
            ctx.context.chessBoard.resetBoard();
            ctx.context.playerColor = ChessGame.TeamColor.WHITE;

            return Repl.State.OBSERVING;
        }
    }


    // ----- INVALID COMMAND HELPERS -----

    public static class InvalidCommand extends Command {
        String attemptedCommand;

        public InvalidCommand(String attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print(String.format("Command '%s' not found. Type 'help' for help.", attemptedCommand));
            return ctx.state;
        }
    }

    public static class WrongStateCommand extends Command {
        String attemptedCommand;

        public WrongStateCommand(String attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print(String.format("Command '%s' is not allowed in this state. Type 'help' for help.", attemptedCommand));
            return ctx.state;
        }
    }

    public static class ImproperUsage extends Command {
        Command attemptedCommand;
        ImproperUsageException error;

        public ImproperUsage(Command attemptedCommand, ImproperUsageException error) {
            this.attemptedCommand = attemptedCommand;
            this.error = error;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print(String.format("Invalid usage: %s. Type help for correct usage.", error.getMessage()));
            return ctx.state;
        }
    }
}
