package strategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements LoggerStrategy {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void log(String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.println("[CONSOLE][" + timestamp + "] " + message);
    }
}
