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

            }
        } catch (SQLException e) {
            throw new DataAccessException("inserting new auth failed", e);
        }
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
