package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    private final Set<GameData> records = new HashSet<GameData>();

    public MemoryGameDAO() {
    }

    public boolean add(GameData game) {
        return records.add(game);
    }

    public GameData find(int gameID) {
        for (var record : records) {
            if (record.gameID() == gameID) {
                return record;
            }
        }
        return null;
    }

    public Collection<GameData> getAll() {
        return records;
    }

    public boolean delete(GameData game) {
        return records.remove(game);
    }

    public void clear() {
        records.clear();
    }
}
