package ui;

import java.util.Scanner;

public class ConsoleUI implements UI {
    Scanner scanner;

    public ConsoleUI() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void print(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printError(String msg) {
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + msg + EscapeSequences.RESET_TEXT_COLOR);
    }

    @Override
    public String getCommand(String currentState) {
        System.out.printf("[%s] >>> ", currentState);
        return scanner.nextLine().toLowerCase();
    }
}
