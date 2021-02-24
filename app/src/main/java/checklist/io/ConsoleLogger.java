package checklist.io;

public class ConsoleLogger extends TextIOLogger {
    public ConsoleLogger(boolean debugEnabled) {
        super(new ConsoleTextIO(), debugEnabled);
    }
}
