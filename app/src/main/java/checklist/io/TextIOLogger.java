package checklist.io;

import java.util.function.Supplier;

public class TextIOLogger implements Logger {
    private final InteractiveTextIO io;
    private final boolean debugEnabled;

    public TextIOLogger(InteractiveTextIO io, boolean debugEnabled) {
        this.io = io;
        this.debugEnabled = debugEnabled;
    }

    @Override
    public void debug(String str) {
        if (debugEnabled) {
            io.showWarn(str);
        }
    }

    @Override
    public void debug(Supplier<String> strSupplier) {
        if (debugEnabled) {
            this.debug(strSupplier.get());
        }
    }

    @Override
    public void info(String str) {
        io.show(str);
    }

    @Override
    public void warn(String str) {
        io.showWarn(str);
    }
}
