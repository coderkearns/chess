package client;

import ui.UI;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReplCommands {
    public static record CommandContext(UI ui, ServerFacade facade, Repl.State state, Repl.Context context) {
    }

    public static class ImproperUsageException extends RuntimeException {
    }

    public static abstract class Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN, Repl.State.LOGGED_OUT);
        }

        public abstract Repl.State execute(String[] args, CommandContext ctx);

        protected void validateArgs(String[] args, int expectedLength) {
            if (args.length != expectedLength) {
                throw new ImproperUsageException();
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
            for (var cmd : commandMap.entrySet()) {
                if (!cmd.getValue().getAllowedStates().contains(ctx.state)) {
                    continue;
                }

                ctx.ui.print(cmd.getKey());
            }
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
            ctx.ui.print(String.format("Account created and logged in! (Authorization %s)", ctx.context.authToken));
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
            ctx.ui.print(String.format("Logged in! (Authorization %s)", ctx.context.authToken));
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
            if (!Objects.equals(args[1], "WHITE") && !Objects.equals(args[1], "BLACK")) {
                throw new ImproperUsageException();
            }

            throw new RuntimeException("Not implemented");

            //return ctx.state;
        }
    }

    private static class ObserveGame extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_IN);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 1);

            throw new RuntimeException("Not implemented");

            //return ctx.state;
        }
    }


    // ----- INVALID COMMAND HELPERS -----

    public static class InvalidCommand extends Command {
        String attemptedCommand;

        public InvalidCommand(String attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print(String.format("Command '%s' not found. Type Help for help.", attemptedCommand));
            return ctx.state;
        }
    }

    public static class WrongStateCommand extends Command {
        String attemptedCommand;

        public WrongStateCommand(String attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print(String.format("Command '%s' is not allowed in this state. Type Help for help.", attemptedCommand));
            return ctx.state;
        }
    }

    public static class ImproperUsage extends Command {
        Command attemptedCommand;

        public ImproperUsage(Command attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            ctx.ui.print("Invalid usage. Type help for correct usage.");
            return ctx.state;
        }
    }
}
