package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    public final Map<String, String> records = new HashMap<String, String>();

    public MemoryAuthDAO() {
    }

    public boolean add(AuthData auth) {
        if (records.containsKey(auth.authToken())) {
            return false;
        }
        records.put(auth.authToken(), auth.username());
        return true;
    }

    public boolean exists(AuthData auth) {
        return records.containsKey(auth.authToken());
    }

    public String getUsername(String authToken) {
        return records.get(authToken);
    }

    public boolean delete(AuthData auth) {
        var removed = records.remove(auth.authToken());
        return removed != null;
    }

    public void clear() {
        records.clear();
    }
}
