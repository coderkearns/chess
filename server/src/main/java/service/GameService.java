package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import exceptions.Exceptions;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {
    final GameDAO gameDAO;
    final AuthDAO authDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
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

    public ListGamesResponse listGames() throws DataAccessException {
        var games = new ArrayList<GameResponse>();

        for (var game : gameDAO.getAll()) {
            games.add(new GameResponse(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }

        return new ListGamesResponse(games);
    }

    public NewGameResponse newGame(NewGameRequest req) throws DataAccessException {
        var newGame = new GameData(-1, null, null, req.gameName(), new ChessGame());
        newGame = gameDAO.add(newGame);
        return new NewGameResponse(newGame.gameID());
    }

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException {
        if (!Objects.equals(req.playerColor(), "WHITE") && !Objects.equals(req.playerColor(), "BLACK")) {
            throw new Exceptions.BadRequestException();
        }

        var game = gameDAO.find(req.gameID());
        if (game == null) {
            throw new Exceptions.BadRequestException();
        }

        String current = req.playerColor().equals("WHITE") ? game.whiteUsername() : game.blackUsername();
        if (current != null) {
            throw new Exceptions.AlreadyTakenException();
        }

        String playerUsername = authDAO.getUsername(authToken);

        String newWhiteUsername = req.playerColor().equals("WHITE") ? playerUsername : game.whiteUsername();
        String newBlackUsername = req.playerColor().equals("BLACK") ? playerUsername : game.blackUsername();

        gameDAO.setGame(new GameData(game.gameID(), newWhiteUsername, newBlackUsername, game.gameName(), game.game()));
    }

    public void clear() throws DataAccessException {
        gameDAO.clear();
    }
}
