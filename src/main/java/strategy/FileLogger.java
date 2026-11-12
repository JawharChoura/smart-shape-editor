package strategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileLogger implements LoggerStrategy {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Path logPath;

    public FileLogger(Path logPath) {
        this.logPath = logPath;
    }

    @Override
    public void log(String message) {
        try {
            if (logPath.getParent() != null) {
                Files.createDirectories(logPath.getParent());
            }
            String line = "[FILE][" + LocalDateTime.now().format(FORMATTER) + "] " + message + System.lineSeparator();
            Files.writeString(logPath, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'ecrire dans le fichier de log", e);
        }
    }
}
