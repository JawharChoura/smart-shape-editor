package strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import utils.DBUtil;

public class DBLogger implements LoggerStrategy {

    private static final String INSERT_LOG = "INSERT INTO logs(timestamp, message) VALUES(?, ?)";

    @Override
    public void log(String message) {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_LOG)) {
            statement.setString(1, LocalDateTime.now().toString());
            statement.setString(2, message);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Impossible d'ecrire dans la base de donnees", e);
        }
    }
}
