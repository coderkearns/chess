package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import client.ServerFacade;

import java.util.List;
import java.util.Set;

public interface UI {

    public void print(String msg);

    public void printError(String msg);

    public void printGameList(List<ServerFacade.GameResponse> games);

    public void showChessBoard(ChessBoard board, ChessGame.TeamColor myColor, Set<ChessPosition> highlighted);

    public String getCommand(String currentState);
}
