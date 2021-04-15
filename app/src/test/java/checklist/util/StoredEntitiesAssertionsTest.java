package checklist.util;

import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

import checklist.TestUtils;
import checklist.io.ConsoleLogger;
import checklist.io.Logger;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class StoredEntitiesAssertionsTest {
    private static final Logger LOG = new ConsoleLogger(false);

    @Test
    public void testEqualStrings() {
        StoredEntitiesAssertions.assertEquals(new StoredString("test"), new StoredString("test"));
    }

    @Test
    public void testDifferentStrings() {
        MultipleFailuresError ex = assertThrows(MultipleFailuresError.class,
                () -> StoredEntitiesAssertions.assertEquals(
                        new StoredString("test"),
                        new StoredString("other")));
        String msg = ex.getMessage();
        LOG.debug(() -> "error message: " + msg);
        String[] lines = TestUtils.toLines(msg);
        assertEquals(2, lines.length);
        assertTrue(lines[0].contains("(1 failure)"));
        assertTrue(lines[1].contains("expected: <test> but was: <other>"));
    }

    @Test
    public void testDifferentWithMessage() {
        MultipleFailuresError ex = assertThrows(MultipleFailuresError.class,
                () -> StoredEntitiesAssertions.assertEquals("compare StoredString:",
                        new StoredString("test"),
                        new StoredString("other")));
        String msg = ex.getMessage();
        LOG.debug(() -> "error message: " + msg);
        String[] lines = TestUtils.toLines(msg);
        assertEquals(2, lines.length);
        assertTrue(lines[0].contains("compare StoredString: (1 failure)"));
        assertTrue(lines[1].contains("expected: <test> but was: <other>"));
    }

    @Test
    public void testEqualMaps() {
        StoredEntitiesAssertions.assertEquals(
                new StoredMap().put("a", "1").put("b", "2"),
                new StoredMap().put("a", "1").put("b", "2"));
    }

    @Test
    public void testDifferentMaps() {
        MultipleFailuresError ex = assertThrows(MultipleFailuresError.class,
                () -> StoredEntitiesAssertions.assertEquals(
                        new StoredMap().put("a", "1").put("b", "2"),
                        new StoredMap().put("a", "1").put("b", "3").put("c", "4")));
        String msg = ex.getMessage();
        LOG.debug(() -> "error message: " + msg);
        String[] lines = TestUtils.toLines(msg);
        assertEquals(5, lines.length);
        assertTrue(lines[1].contains("maps (2 failures)"));
        assertTrue(lines[2].contains("expected: <HashSet(a, b)> but was: <HashSet(a, b, c)>"));
        assertTrue(lines[3].contains("values for key b (1 failure)"));
        assertTrue(lines[4].contains("expected: <2> but was: <3>"));
    }

    @Test
    public void testEqualNestedMaps() {
        StoredEntitiesAssertions.assertEquals(
                new StoredMap().put("a", "1")
                        .put("b", new StoredMap().put("c", "2")
                                .put("d", "3")),
                new StoredMap().put("a", "1")
                        .put("b", new StoredMap().put("c", "2")
                                .put("d", "3")));
    }

    @Test
    public void testDifferentNestedMaps() {
        MultipleFailuresError ex = assertThrows(MultipleFailuresError.class,
                () -> StoredEntitiesAssertions.assertEquals("nested maps",
                        new StoredMap().put("a", "1")
                                .put("b", new StoredMap().put("c", "2")
                                        .put("d", "3")
                                        .put("e", "4")),
                        new StoredMap().put("a", "1")
                                .put("b", new StoredMap().put("c", "6")
                                        .put("d", "3")
                                        .put("f", "7"))));
        String msg = ex.getMessage();
        LOG.debug(() -> "error message: " + msg);
        String[] lines = TestUtils.toLines(msg);
        // TODO use assertj
        assertEquals(7, lines.length);
        assertTrue(lines[0].startsWith("nested maps (1 failure)"));
        assertTrue(lines[1].contains("maps (1 failure)"));
        assertTrue(lines[2].contains("values for key b (1 failure)"));
        assertTrue(lines[3].contains("maps (2 failures)"));
        assertTrue(lines[4].contains("expected: <HashSet(c, d, e)> but was: <HashSet(c, d, f)>"));
        assertTrue(lines[5].contains("values for key c (1 failure)"));
        assertTrue(lines[6].contains("expected: <2> but was: <6>"));
    }
}