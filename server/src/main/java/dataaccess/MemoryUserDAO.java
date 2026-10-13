package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    public final HashMap<String, UserData> records = new HashMap<String, UserData>();

    public MemoryUserDAO() {
    }

    public boolean add(UserData user) {
        if (records.containsKey(user.username())) {
            return false;
        }
        records.put(user.username(), user);
        return true;
    }

    public UserData find(String username) {
        return records.get(username);
    }

    public void clear() {
        records.clear();
    }
}
