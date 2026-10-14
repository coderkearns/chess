package client;

import ui.ConsoleUI;
import ui.UI;

public class Repl {
    public enum State {
        LOGGED_OUT,
        LOGGED_IN,
        QUIT
    }

    public static class Context {
        public String authToken = null;
    }

    private final UI ui;
    private State state;
    private Context context;

    public Repl(String serverUrl) {
        ui = new ConsoleUI();
    }

    public void run() {
        initializeState();

        ui.print("♕ Welcome to CS 240 Chess Client. Type Help for help. ♕");

        while (state != State.QUIT) {
            var commandName = ui.getCommand(state.toString());
            var command = ReplCommands.commandMap.get(commandName);

            if (command == null) command = new ReplCommands.InvalidCommand(commandName);
            if (!command.getAllowedStates().contains(state)) command = new ReplCommands.WrongStateCommand(commandName);

            state = command.execute(ui, state, context);
        }
    }

    private void initializeState() {
        state = State.LOGGED_OUT;
        context = new Context();
    }
}
