package dataaccess;

import model.GameData;

import java.util.HashSet;
import java.util.Set;

public class MemoryGameDAO implements GameDAO {
    private final Set<GameData> records = new HashSet<GameData>();

    public MemoryGameDAO() {
    }

    public boolean add(GameData game) {
        return records.add(game);
    }

    public boolean exists(GameData game) {
        return records.contains(game);
    }

    public boolean delete(GameData game) {
        return records.remove(game);
    }

    public void clear() {
        records.clear();
    }
}
