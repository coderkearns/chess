package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

public class SQLAuthDAO extends SQLDAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        configureDatabase(new String[]{
                """
                CREATE TABLE IF NOT EXISTS auth (
                    `token` VARCHAR(255) NOT NULL PRIMARY KEY,
                    `username` VARCHAR(255) NOT NULL
                );
                """
        });
    }

    @Override
    public boolean add(AuthData auth) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("INSERT IGNORE INTO auth (token, username) VALUES (?, ?)")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();

                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        });
    }

    @Override
    public boolean tokenExists(AuthData auth) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("SELECT token FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, auth.authToken());
                var resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        });
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, authToken);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("username");
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public boolean delete(AuthData auth) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        });
    }

    @Override
    public void clear() throws DataAccessException {
        withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
            return null;
        });
    }
}
