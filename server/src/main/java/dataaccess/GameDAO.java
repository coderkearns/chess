package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /**
     * Stores a new game in the data layer. Returns true if it was added successfully, or false if it already exists.
     */
    boolean add(GameData game) throws DataAccessException;

    /**
     * Finds the specified game, or null if it doesn't exist.
     */
    GameData find(int gameID) throws DataAccessException;

    /**
     * Sets a game with data, overriding the previous data.
     */
    void setGame(GameData game) throws DataAccessException;

    /**
     * Returns all the games in the data layer.
     */
    Collection<GameData> getAll() throws DataAccessException;

    /**
     * Removes a game from the data layer. Returns true if it existed and was deleted, or false if it didn't exist.
     */
    boolean delete(GameData game) throws DataAccessException;

    /**
     * Wipes all games from the data layer.
     */
    void clear() throws DataAccessException;

}
