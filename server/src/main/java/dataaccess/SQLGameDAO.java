package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
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
                            resultSet.getString("white_username"),
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

    }

    @Override
    public Collection<GameData> getAll() throws DataAccessException {
        return List.of();
    }

    @Override
    public boolean delete(GameData game) throws DataAccessException {
        return false;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
