package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private int nextID = 1;

    public final Map<Integer, GameData> records = new HashMap<Integer, GameData>();

    public MemoryGameDAO() {
    }

    public GameData add(GameData game) {
        var newGame = new GameData(nextID++, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
        records.put(newGame.gameID(), newGame);
        return newGame;
    }

    public void setGame(GameData game) {
        records.put(game.gameID(), game);
    }

    public GameData find(int gameID) {
        return records.get(gameID);
    }

    public Collection<GameData> getAll() {
        return records.values();
    }

    public boolean delete(GameData game) {
        var removed = records.remove(game.gameID());
        return removed != null;
    }

    public void clear() {
        records.clear();
    }
}
