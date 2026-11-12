package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBUtil {

    private static final String DB_URL = "jdbc:sqlite:shapes.db";

    private DBUtil() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initializeDatabase() {
        String createShapesTable = """
            CREATE TABLE IF NOT EXISTS shapes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                x REAL NOT NULL,
                y REAL NOT NULL,
                width REAL NOT NULL,
                height REAL NOT NULL
            )
            """;

        String createLogsTable = """
            CREATE TABLE IF NOT EXISTS logs (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                timestamp TEXT NOT NULL,
                message TEXT NOT NULL
            )
            """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createShapesTable);
            statement.execute(createLogsTable);
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'initialisation de la base", e);
        }
    }
}
