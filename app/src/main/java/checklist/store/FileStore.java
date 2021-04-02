package checklist.store;

import java.io.File;
import java.io.IOException;

import checklist.io.FileIO;
import checklist.store.model.StoredEntity;
import io.vavr.control.Either;

public class FileStore implements Store {
    public final String fileExtension;
    protected final File workDir;
    protected final StoreFormat format;

    public FileStore(String fileExtension, File workDir, StoreFormat format) {
        this.fileExtension = fileExtension;
        this.workDir = workDir;
        this.format = format;
    }

    @Override
    public String getName(String path) {
        return removeFileExtension(path);
    }

    String removeFileExtension(String name) {
        return name.endsWith(fileExtension)
                ? name.substring(0, name.length() - fileExtension.length())
                : name;
    }

    String addFileExtension(String name) {
        return name + fileExtension;
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
