package checklist.store;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import checklist.io.FileIO;
import checklist.json.ObjectMapperFactory;
import checklist.store.model.StoredEntity;
import checklist.store.model.StoredEntityVisitor;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;
import io.vavr.control.Either;

// TODO extract FileStore
public class ChecklistStore implements Store {
    public static final String FILE_EXTENSION = ".checklist";

    private final File workDir;
    private final StoreFormat format;

    public ChecklistStore(File workDir, ObjectMapperFactory objectMapperFactory) {
        this.workDir = workDir;
        this.format = new JsonStoreFormat(objectMapperFactory);
    }

    @Override
    public String getName(String path) {
        return removeFileExtension(path);
    }

    static String removeFileExtension(String name) {
        return name.endsWith(FILE_EXTENSION)
                ? name.substring(0, name.length() - FILE_EXTENSION.length())
                : name;
    }

    static String addFileExtension(String name) {
        return name + FILE_EXTENSION;
    }

    @Override
    public Either<String, String> save(String path, StoredEntity entity) {
        File file = new File(workDir, addFileExtension(removeFileExtension(path)));
        try {
            boolean created = file.createNewFile();
            if (!created) {
                return Either.left(String.format("File '%s' already exists", file.getName()));
            }
            new FileIO(file).write(out -> format.write(entity, out));
            return Either.right(file.getName());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
