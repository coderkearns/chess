package dataaccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public boolean add(AuthData auth) {
        return false;
    }

    @Override
    public boolean tokenExists(AuthData auth) {
        return false;
    }

    @Override
    public String getUsername(String authToken) {
        return "";
    }

    @Override
    public boolean delete(AuthData auth) {
        return false;
    }

    @Override
    public void clear() {

    }
}
