package client;

import ui.UI;

import java.util.Map;
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
            "login", new Login()
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

    private static class Login extends Command {
        public Set<Repl.State> getAllowedStates() {
            return Set.of(Repl.State.LOGGED_OUT);
        }

        public Repl.State execute(String[] args, CommandContext ctx) {
            validateArgs(args, 2);
            ctx.ui.print("Logging in...");
            ctx.facade.login(args[0], args[1]);
            ctx.ui.print("Logged in!");
            return Repl.State.LOGGED_IN;
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
