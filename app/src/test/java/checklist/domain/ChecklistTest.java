package checklist.domain;

import checklist.store.model.StoredMap;
import checklist.util.StoredEntitiesAssertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChecklistTest {

    @Test
    void toStored1() {
        StoredMap actual = new Checklist("1", "test").toStored();
        StoredMap expected = new StoredMap().put("id", "1").put("name", "test");
        StoredEntitiesAssertions.assertEquals(expected, actual);
    }

    @Test
    void toStored2() {
        StoredMap actual = new Checklist("2", "test2").toStored();
        StoredMap expected = new StoredMap().put("id", "2").put("name", "test2");
        StoredEntitiesAssertions.assertEquals(expected, actual);
    }

    @Test
    void fromStored1() {
        StoredMap stored = new StoredMap().put("id", "1").put("name", "test");
        Checklist actual = Checklist.fromStored(stored);
        Checklist expected = new Checklist("1", "test");
        assertChecklistEquals(expected, actual);
    }

    @Test
    void fromStored2() {
        StoredMap stored = new StoredMap().put("id", "2").put("name", "test2");
        Checklist actual = Checklist.fromStored(stored);
        Checklist expected = new Checklist("2", "test2");
        assertChecklistEquals(expected, actual);
    }

    private void assertChecklistEquals(Checklist expected, Checklist actual) {
        assertAll(
                () -> assertEquals(expected.getId(), actual.getId(), "id"),
                () -> assertEquals(expected.getName(), actual.getName(), "name"));
    }
}