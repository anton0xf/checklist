package checklist.util;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AssertUtils {
    public static <T> void assertInstanceOf(Object value, Class<T> clazz, Consumer<T> fn) {
        assertTrue(clazz.isInstance(value));
        fn.accept(clazz.cast(value));
    }
}
