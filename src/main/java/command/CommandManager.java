package command;

import java.util.ArrayDeque;
import java.util.Deque;

public class CommandManager {

    private final Deque<Command> history = new ArrayDeque<>();

    public void executeCommand(Command command) {
        command.execute();
        history.push(command);
    }

    public Command undo() {
        if (history.isEmpty()) {
            return null;
        }
        Command command = history.pop();
        command.undo();
        return command;
    }

    public boolean canUndo() {
        return !history.isEmpty();
    }

    public void clearHistory() {
        history.clear();
    }
}
