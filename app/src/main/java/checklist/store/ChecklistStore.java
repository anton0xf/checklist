package checklist.store;

import java.io.File;

import checklist.json.ObjectMapperFactory;

public class ChecklistStore extends FileStore {
    public static final String FILE_EXTENSION = ".checklist";

    public ChecklistStore(File workDir, ObjectMapperFactory objectMapperFactory) {
        super(FILE_EXTENSION, workDir, new JsonStoreFormat(objectMapperFactory));
    }
}
