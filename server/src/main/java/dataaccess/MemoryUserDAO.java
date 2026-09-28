package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {
    private final Set<UserData> records = new HashSet<UserData>();

    public MemoryUserDAO() {
    }

    public boolean add(UserData user) {
        return records.add(user);
    }

    public boolean exists(UserData user) {
        return records.contains(user);
    }

    public boolean delete(UserData user) {
        return records.remove(user);
    }

    public void clear() {
        records.clear();
    }

}
