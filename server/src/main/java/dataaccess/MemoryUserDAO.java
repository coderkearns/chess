package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MemoryUserDAO implements UserDAO {
    public final Set<UserData> records = new HashSet<UserData>();

    public MemoryUserDAO() {
    }

    public boolean add(UserData user) {
        return records.add(user);
    }
    
    public UserData find(String username) {
        for (var record : records) {
            if (Objects.equals(record.username(), username)) {
                return record;
            }
        }
        return null;
    }

    public void clear() {
        records.clear();
    }
}
