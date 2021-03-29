package checklist.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChecklistStoreTest {
    @Test
    public void addFileExtension() {
        assertEquals(".checklist", ChecklistStore.addFileExtension(""));
        assertEquals("asdf.checklist", ChecklistStore.addFileExtension("asdf"));
    }

    @Test
    public void removeFileExtension() {
        assertEquals("", ChecklistStore.removeFileExtension(""));
        assertEquals("a", ChecklistStore.removeFileExtension("a"));
        assertEquals("asdf", ChecklistStore.removeFileExtension("asdf"));
        assertEquals("", ChecklistStore.removeFileExtension(".checklist"));
        assertEquals("a", ChecklistStore.removeFileExtension("a.checklist"));
        assertEquals("asdf", ChecklistStore.removeFileExtension("asdf.checklist"));
    }
}