package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO extends SQLDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        configureDatabase(new String[]{
                """
                CREATE TABLE IF NOT EXISTS games (
                    `id` INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
                    `name` VARCHAR(255) NOT NULL,
                    `white_username` VARCHAR(255) DEFAULT NULL,
                    `white_username` VARCHAR(255) DEFAULT NULL,
                    `game_json` JSON
                );
                """
        });
    }

    @Override
    public boolean add(GameData game) throws DataAccessException {
        return false;
    }

    @Override
    public GameData find(int gameID) throws DataAccessException {
        return null;
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
