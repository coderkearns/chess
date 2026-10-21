package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import client.ServerFacade;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UI {
    Scanner scanner;

    @Override
    public String getCommand(String currentState) {
        System.out.printf("[%s] >>> ", currentState);
        return scanner.nextLine().toLowerCase();
    }

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
    public void showChessBoard(ChessBoard board, ChessGame.TeamColor myColor) {
        printLettersRow(myColor);
        if (myColor == ChessGame.TeamColor.WHITE) {
            for (int row = 1; row < 9; row++) {
                System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row);
                for (int col = 1; col < 9; col++) {
                    printBoardCell(board, row, col);
                }
                System.out.printf("%s %d %s\n", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row, EscapeSequences.RESET_BG_COLOR);
            }
        } else {
            for (int row = 8; row > 0; row--) {
                System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row);
                for (int col = 8; col > 0; col--) {
                    printBoardCell(board, row, col);
                }
                System.out.printf("%s %d %s\n", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row, EscapeSequences.RESET_BG_COLOR);
            }
        }
        printLettersRow(myColor);
    }

    private void printLettersRow(ChessGame.TeamColor myColor) {
        String letters = "    a  b  c  d  e  f  g  h    ";
        if (myColor == ChessGame.TeamColor.BLACK) {
            StringBuilder lettersBuffer = new StringBuilder(letters);
            lettersBuffer.reverse();
            letters = lettersBuffer.toString();
        }
        System.out.printf("%s%s%s\n", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, letters, EscapeSequences.RESET_BG_COLOR);
    }

    private void printBoardCell(ChessBoard board, int row, int col) {
        var bgColor = (row + col) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_BLACK : EscapeSequences.SET_BG_COLOR_WHITE;
        var piece = board.getPiece(new ChessPosition(row, col));
        if (piece == null) {
            System.out.printf("%s   %s", bgColor, EscapeSequences.RESET_BG_COLOR);
        } else {
            var fgColor = piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.SET_TEXT_COLOR_BLUE : EscapeSequences.SET_TEXT_COLOR_RED;
            System.out.printf("%s%s %s %s%s", bgColor, fgColor, "#", EscapeSequences.RESET_BG_COLOR, EscapeSequences.RESET_TEXT_COLOR);
        }
    }
}
