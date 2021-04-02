package checklist.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileStoreTest {
    @Test
    public void addFileExtension() {
        FileStore store = new FileStore(".checklist", null, null);
        assertEquals(".checklist", store.addFileExtension(""));
        assertEquals("asdf.checklist", store.addFileExtension("asdf"));
    }

    @Test
    public void removeFileExtension() {
        FileStore store = new FileStore(".checklist", null, null);
        assertEquals("", store.removeFileExtension(""));
        assertEquals("a", store.removeFileExtension("a"));
        assertEquals("asdf", store.removeFileExtension("asdf"));
        assertEquals("", store.removeFileExtension(".checklist"));
        assertEquals("a", store.removeFileExtension("a.checklist"));
        assertEquals("asdf", store.removeFileExtension("asdf.checklist"));
    }
}