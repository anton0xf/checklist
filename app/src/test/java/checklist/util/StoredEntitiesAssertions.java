package checklist.util;

import org.junit.jupiter.api.Assertions;

import checklist.store.model.StoredEntity;
import checklist.store.model.StoredEntityVisitor;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;

// TODO test it
public class StoredEntitiesAssertions {
    public static void assertEquals(StoredEntity expected, StoredEntity actual) {
        assertEquals(null, expected, actual);
    }

    public static void assertEquals(String msg, StoredEntity expected, StoredEntity actual) {
        Assertions.assertAll(msg,
                () -> expected.visit(new StoredEntityVisitor<Void>() {
                    @Override
                    public Void visitString(StoredString str) {
                        AssertionsUtils.assertInstanceOf(actual, StoredString.class,
                                actualStr -> Assertions.assertEquals(str.get(), actualStr.get()));
                        return null;
                    }

                    @Override
                    public Void visitMap(StoredMap map) {
                        AssertionsUtils.assertInstanceOf(actual, StoredMap.class,
                                actualMap -> assertEquals(map, actualMap));
                        return null;
                    }
                }));
    }

    private static void assertEquals(StoredMap expected, StoredMap actual) {
        assertEquals("maps", expected, actual);
    }

    private static void assertEquals(String msg, StoredMap expected, StoredMap actual) {
        Assertions.assertAll(msg,
                () -> Assertions.assertEquals(expected.keys(), actual.keys()),
                () -> expected.iterator()
                        .forEach(kv -> assertEquals("values for key " + kv._1,
                                kv._2, actual.get(kv._1))));
    }
}
