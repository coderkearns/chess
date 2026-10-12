package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public boolean add(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT IGNORE INTO auth (token, username) VALUES (?, ?)")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());
                preparedStatement.executeUpdate();

                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        } catch (SQLException e) {
            throw new DataAccessException("inserting new auth failed", e);
        }
    }

    @Override
    public boolean tokenExists(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT token FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, auth.authToken());
                var resultSet = preparedStatement.executeQuery();
                return resultSet != null && resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("finding auth failed", e);
        }
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, authToken);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet != null && resultSet.next()) {
                    return resultSet.getString("username");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("finding auth failed", e);
        }
    }

    @Override
    public boolean delete(AuthData auth) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE token = ?")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.executeUpdate();
                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        } catch (SQLException e) {
            throw new DataAccessException("deleting auth failed", e);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("clearing auth failed", e);
        }
    }

    public final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
                `token` VARCHAR(255) NOT NULL PRIMARY KEY,
                `username` VARCHAR(255) NOT NULL
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to set up the database", e);
        }
    }
}
