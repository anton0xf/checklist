package checklist.store;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import checklist.domain.Checklist;
import checklist.io.FileIO;
import checklist.io.TextIO;
import checklist.json.ObjectMapperFactory;

// TODO extract FileStore superclass
public class ChecklistStore implements Store {
    public static final String FILE_EXTENSION = ".checklist";

    private final TextIO io;
    private final File workDir;

    public ChecklistStore(TextIO io, File workDir) {
        this.io = io;
        this.workDir = workDir;
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
    public void save(String path, Checklist checklist) {
        File file = new File(workDir, addFileExtension(removeFileExtension(path)));
        try {
            file.createNewFile();
            ObjectMapper mapper = ObjectMapperFactory.createMapper();
            // TODO separate model from store
            new FileIO(file).write(out -> mapper.writer().writeValue(out, checklist));
            io.printWarn(String.format("Checklist '%s' created", file.getName()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
