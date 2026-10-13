package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class SQLDAO {
    @FunctionalInterface
    protected interface DBConnectionAction<T> {
        T action(Connection conn) throws SQLException;
    }

    protected <T> T withConnection(DBConnectionAction<T> action) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            return action.action(conn);
        } catch (SQLException e) {
            throw new DataAccessException("failed database operation", e);
        }
    }

    protected void configureDatabase(String[] createStatements) throws DataAccessException {
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
