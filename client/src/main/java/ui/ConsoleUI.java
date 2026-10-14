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
    public String getCommand(String currentState) {
        System.out.printf("[%s] >>> ", currentState);
        return scanner.nextLine().toLowerCase();
    }
}
