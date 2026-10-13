package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    Gson gson = new Gson();

    public SQLGameDAO() throws DataAccessException {
        configureDatabase(new String[]{
                """
                CREATE TABLE IF NOT EXISTS games (
                    `id` INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                    `name` VARCHAR(255) NOT NULL,
                    `white_username` VARCHAR(255) DEFAULT NULL,
                    `black_username` VARCHAR(255) DEFAULT NULL,
                    `game_json` JSON
                );
                """
        });
    }

    @Override
    public GameData add(GameData game) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO games (name, white_username, black_username, game_json) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            )) {
                preparedStatement.setString(1, game.gameName());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, gson.toJson(game.game()));
                preparedStatement.executeUpdate();

                try (var resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int gameID = resultSet.getInt(1);
                        return new GameData(
                                gameID,
                                game.whiteUsername(),
                                game.blackUsername(),
                                game.gameName(),
                                game.game()
                        );
                    } else {
                        throw new SQLException("database failed creating new row");
                    }
                }
            }
        });
    }

    @Override
    public GameData find(int gameID) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("SELECT name, white_username, black_username, game_json FROM games WHERE id = ?")) {
                preparedStatement.setInt(1, gameID);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new GameData(
                            gameID,
                            resultSet.getString("white_username"),
                            resultSet.getString("black_username"),
                            resultSet.getString("name"),
                            gson.fromJson(resultSet.getString("game_json"), ChessGame.class)
                    );
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public void setGame(GameData game) throws DataAccessException {
        withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement(
                    "UPDATE games SET white_username = ?, black_username = ?, name = ?, game_json = ? WHERE id = ?"
            )) {
                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, gson.toJson(game.game()));
                preparedStatement.setInt(5, game.gameID());
                preparedStatement.executeUpdate();

                var changedRows = preparedStatement.getUpdateCount();
                if (changedRows != 1) {
                    throw new DataAccessException("game not updated in database");
                }
                return null;
            }
        });
    }

    @Override
    public Collection<GameData> getAll() throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT id, name, white_username, black_username, game_json FROM games"
            )) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    List<GameData> games = new ArrayList<>();
                    while (resultSet.next()) {
                        games.add(new GameData(
                                resultSet.getInt("id"),
                                resultSet.getString("white_username"),
                                resultSet.getString("black_username"),
                                resultSet.getString("name"),
                                gson.fromJson(resultSet.getString("game_json"), ChessGame.class)
                        ));
                    }
                    return games;
                }
            }
        });
    }

    @Override
    public boolean delete(GameData game) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM games WHERE id = ?")) {
                preparedStatement.setInt(1, game.gameID());
                preparedStatement.executeUpdate();
                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        });
    }

    @Override
    public void clear() throws DataAccessException {
        withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
                preparedStatement.executeUpdate();
            }
            return null;
        });
    }
}
