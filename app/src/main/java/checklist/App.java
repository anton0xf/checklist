package checklist;

import checklist.domain.Checklist;
import checklist.io.ConsoleTextIO;
import checklist.io.FileIO;
import checklist.io.TextIO;
import checklist.json.ObjectMapperFactory;
import checklist.util.RandomHashGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.vavr.collection.List;

import java.io.File;
import java.io.IOException;

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final String FILE_EXTENSION = ".checklist";
    public static final int ID_HASH_SIZE = 16;

    // TODO move workDir (supplier?) and hashGenerator to storage
    private final File workDir;
    private final RandomHashGenerator hashGenerator;
    private final TextIO io;

    public static void main(String[] args) {
        File workDir = new File(".");
        RandomHashGenerator hashGenerator = new RandomHashGenerator();
        ConsoleTextIO io = new ConsoleTextIO();
        App app = new App(workDir, hashGenerator, io);
        app.run(args);
    }

    public App(File workDir, RandomHashGenerator hashGenerator, TextIO io) {
        this.workDir = workDir;
        this.hashGenerator = hashGenerator;
        this.io = io;
    }

    void run(String[] args) {
        if (args.length == 0) {
            printHelp();
            System.exit(1);
        }
        String command = args[0];
        List<String> commandArgs = List.of(args).tail();
        switch (command) {
            case CREATE_COMMAND -> create(commandArgs);
            default -> {
                printHelp();
                System.exit(1);
            }
        }
    }

    private void printHelp() {
        // TODO print help message and usage to stderr
        io.printWarn("no args");
    }

    private void create(List<String> args) {
        if (args.isEmpty()) {
            // TODO print command usage
            return;
        }
        String name = removeFileExtension(args.get(0));
        // TODO encapsulate storage
        File file = new File(workDir, addFileExtension(name));
        try {
            file.createNewFile();
            ObjectMapper mapper = ObjectMapperFactory.createMapper();
            String id = hashGenerator.next(ID_HASH_SIZE);
            Checklist checklist = new Checklist(id, name);
            // TODO separate model from store
            new FileIO(file).write(out -> mapper.writer().writeValue(out, checklist));
            io.printWarn(String.format("Checklist '%s' created", file.getName()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static String removeFileExtension(String name) {
        return name.endsWith(FILE_EXTENSION)
                ? name.substring(0, name.length() - FILE_EXTENSION.length())
                : name;
    }

    static String addFileExtension(String name) {
        return name + FILE_EXTENSION;
    }
}
