package ui;

import client.ServerFacade;

import java.util.List;
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
    public void printGameList(List<ServerFacade.GameResponse> games) {
        for (var game : games) {
            System.out.printf("%d %s\t[white=%s, black=%s]\n", game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername());
        }
    }

    @Override
    public String getCommand(String currentState) {
        System.out.printf("[%s] >>> ", currentState);
        return scanner.nextLine().toLowerCase();
    }
}
