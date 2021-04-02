package checklist.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import checklist.store.model.StoredEntity;
import checklist.store.model.StoredEntityVisitor;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;
import io.vavr.collection.Stream;

public class StoredEntitiesAssertions {
    public static void assertEquals(StoredEntity expected, StoredEntity actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String msg, StoredEntity expected, StoredEntity actual) {
        Assertions.assertAll(msg,
                () -> expected.visit(new StoredEntityVisitor<Void>() {
                    @Override
                    public Void visitString(StoredString str) {
                        AssertUtils.assertInstanceOf(actual, StoredString.class,
                                actualStr -> Assertions.assertEquals(str.get(), actualStr.get()));
                        return null;
                    }

                    @Override
                    public Void visitMap(StoredMap map) {
                        AssertUtils.assertInstanceOf(actual, StoredMap.class,
                                actualMap -> assertEquals(map, actualMap));
                        return null;
                    }
                }));
    }

    private static void assertEquals(StoredMap expected, StoredMap actual) {
        Assertions.assertAll("maps",
                Stream.<Executable>empty()
                        .append(() -> Assertions.assertEquals(expected.keys(), actual.keys()))
                        .appendAll(expected.keys().intersect(actual.keys())
                                .map(key -> () -> assertEquals("values for key " + key,
                                        expected.get(key), actual.get(key))))
                        .toJavaStream());
    }
}
