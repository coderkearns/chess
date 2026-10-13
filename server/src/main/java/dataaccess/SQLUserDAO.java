package dataaccess;

import model.UserData;

public class SQLUserDAO extends SQLDAO implements UserDAO {
    public SQLUserDAO() throws DataAccessException {
        configureDatabase(new String[]{
                """
                CREATE TABLE IF NOT EXISTS users (
                    `username` VARCHAR(255) NOT NULL PRIMARY KEY,
                    `email` VARCHAR(255) UNIQUE NOT NULL,
                    `password` VARCHAR(255) NOT NULL
                );
                """
        });
    }

    @Override
    public boolean add(UserData user) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("INSERT IGNORE INTO users (username, email, password) VALUES (?, ?, ?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.email());
                preparedStatement.setString(3, user.password());
                preparedStatement.executeUpdate();

                var changedRows = preparedStatement.getUpdateCount();
                return changedRows == 1;
            }
        });
    }

    @Override
    public UserData find(String username) throws DataAccessException {
        return withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("SELECT email, password FROM users WHERE username = ?")) {
                preparedStatement.setString(1, username);
                var resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return new UserData(
                            username,
                            resultSet.getString("password"),
                            resultSet.getString("email")
                    );
                } else {
                    return null;
                }
            }
        });
    }

    @Override
    public void clear() throws DataAccessException {
        withConnection(conn -> {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
            return null;
        });
    }
}
