package ui;

import chess.ChessBoard;
import chess.ChessGame;
import client.ServerFacade;

import java.util.List;

public interface UI {

    public void print(String msg);

    public void printError(String msg);

    public void printGameList(List<ServerFacade.GameResponse> games);

    public void showChessBoard(ChessBoard board, ChessGame.TeamColor myColor);

    public String getCommand(String currentState);
}
