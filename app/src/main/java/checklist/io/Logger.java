package checklist.io;

import java.util.function.Supplier;

/**
 * So primitive logger (without loggers hierarchy, many log levels etc.) is enough for simple CLI app
 */
public interface Logger {
    void debug(String str);
    void info(String str);
    void warn(String str);

    /**
     * Implementations can disable debug logging without actual string computation
     */
    default void debug(Supplier<String> strSupplier) {
        this.debug(strSupplier.get());
    }
}
