package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDAO implements AuthDAO {
    private final Set<AuthData> records = new HashSet<AuthData>();

    public MemoryAuthDAO() {
    }

    public boolean add(AuthData auth) {
        return records.add(auth);
    }

    public boolean exists(AuthData auth) {
        return records.contains(auth);
    }

    public boolean delete(AuthData auth) {
        return records.remove(auth);
    }

    public void clear() {
        records.clear();
    }
}
