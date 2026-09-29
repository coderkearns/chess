package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class GameService {
    final GameDAO gameDAO;

    private int nextGameID;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        nextGameID = 1;
    }

    /* --- DTOs --- */

    public static record NewGameRequest(String gameName) {
    }

    public static record NewGameResponse(int gameID) {
    }

    public static record JoinGameRequest(String playerColor, int gameID) {
    }

    public static record GameResponse(int gameID, String whiteUsername, String blackUsername, String gameName) {
    }

    public static record ListGamesResponse(Collection<GameResponse> games) {
    }

    /* --- Methods --- */

    public ListGamesResponse listGames() {
        var games = new ArrayList<GameResponse>();

        for (var game : gameDAO.getAll()) {
            games.add(new GameResponse(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }

        return new ListGamesResponse(games);
    }

    public NewGameResponse newGame(NewGameRequest req) {
        var newGame = new GameData(nextGameID++, null, null, req.gameName(), new ChessGame());
        gameDAO.add(newGame);
        return new NewGameResponse(newGame.gameID());
    }

    public void joinGame(JoinGameRequest req) {
    }

    public void clear() {
        gameDAO.clear();
    }
}
