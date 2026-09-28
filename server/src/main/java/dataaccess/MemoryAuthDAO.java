package dataaccess;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDAO implements AuthDAO {
    private final Set<String> records = new HashSet<String>();

    public MemoryAuthDAO() {
    }

    public boolean add(String authToken) {
        return records.add(authToken);
    }

    public boolean exists(String authToken) {
        return records.contains(authToken);
    }

    public boolean delete(String authToken) {
        return records.remove(authToken);
    }

    public void clear() {
        records.clear();
    }
}
