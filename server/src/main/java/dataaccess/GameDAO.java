package dataaccess;

import model.GameData;

public interface GameDAO {
    /**
     * Stores a new game in the data layer. Returns true if it was added successfully, or false if it already exists.
     */
    boolean add(GameData game);

    /**
     * Checks if a game is stored in the data layer.
     */
    boolean exists(GameData game);

    /**
     * Removes a game from the data layer. Returns true if it existed and was deleted, or false if it didn't exist.
     */
    boolean delete(GameData game);

    /**
     * Wipes all games from the data layer.
     */
    void clear();

}
