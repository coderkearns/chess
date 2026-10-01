package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> records = new HashMap<Integer, GameData>();

    public MemoryGameDAO() {
    }

    public boolean add(GameData game) {
        if (records.containsKey(game.gameID())) {
            return false;
        }
        records.put(game.gameID(), game);
        return true;
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
