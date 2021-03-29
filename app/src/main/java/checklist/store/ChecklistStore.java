package checklist.store;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import checklist.domain.Checklist;
import checklist.io.FileIO;
import checklist.json.ObjectMapperFactory;
import io.vavr.control.Either;

// TODO extract FileStore superclass
public class ChecklistStore implements Store {
    public static final String FILE_EXTENSION = ".checklist";

    private final File workDir;
    private final ObjectMapper mapper;

    public ChecklistStore(File workDir, ObjectMapperFactory objectMapperFactory) {
        this.workDir = workDir;
        this.mapper = objectMapperFactory.createMapper();
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
    public Either<String, String> save(String path, Checklist checklist) {
        File file = new File(workDir, addFileExtension(removeFileExtension(path)));
        try {
            boolean created = file.createNewFile();
            // TODO separate model from store
            new FileIO(file).write(out -> mapper.writer().writeValue(out, checklist));
            return Either.right(file.getName());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
