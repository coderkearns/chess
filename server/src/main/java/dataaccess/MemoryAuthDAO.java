package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Set;

public class MemoryAuthDAO implements AuthDAO {
    private final Set<String> records = new HashSet<String>();

    public MemoryAuthDAO() {
    }

    public boolean add(AuthData auth) {
        return records.add(auth.authToken());
    }

    public boolean exists(AuthData auth) {
        return records.contains(auth.authToken());
    }

    public boolean delete(AuthData auth) {
        return records.remove(auth.authToken());
    }

    public void clear() {
        records.clear();
    }
}
