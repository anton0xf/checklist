package checklist.store;

import checklist.store.model.StoredEntity;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface StoreFormat {
    void write(StoredEntity entity, Writer out) throws IOException;
    StoredEntity read(Reader in) throws IOException;
}
