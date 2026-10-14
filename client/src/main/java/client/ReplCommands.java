package client;

import ui.UI;

import java.util.Map;
import java.util.Set;

public class ReplCommands {
    public static abstract class Command {
        public Set<Repl.State> allowedStates;

        public abstract Repl.State execute(UI ui, Repl.State state, Repl.Context context);
    }

    public static Map<String, Command> commandMap = Map.of(
            "quit", new Quit()
    );

    // ----- COMMANDS -----

    private static class Quit extends Command {
        public Set<Repl.State> allowedStates = Set.of(Repl.State.LOGGED_IN, Repl.State.LOGGED_OUT);

        public Repl.State execute(UI ui, Repl.State state, Repl.Context context) {
            ui.print("Bye!");
            return Repl.State.QUIT;
        }
    }


    // ----- INVALID COMMAND HELPER -----

    public static class InvalidCommand extends Command {
        String attemptedCommand;

        public InvalidCommand(String attemptedCommand) {
            this.attemptedCommand = attemptedCommand;
        }

        public Repl.State execute(UI ui, Repl.State state, Repl.Context context) {
            ui.print(String.format("Command '%s' not found. Type Help for help.", attemptedCommand));
            return state;
        }
    }
}
