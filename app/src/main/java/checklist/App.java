package checklist;

import java.io.File;

import checklist.domain.Checklist;
import checklist.io.ConsoleTextIO;
import checklist.io.TextIO;
import checklist.store.ChecklistStore;
import checklist.store.Store;
import checklist.util.RandomHashGenerator;
import io.vavr.collection.List;

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final int ID_HASH_SIZE = 16;

    private final TextIO io;
    private final Store store;
    private final RandomHashGenerator hashGenerator;

    public static void main(String[] args) {
        ConsoleTextIO io = new ConsoleTextIO();
        RandomHashGenerator hashGenerator = new RandomHashGenerator(ID_HASH_SIZE);
        File workDir = new File(".");
        Store store = new ChecklistStore(io, workDir);
        App app = new App(io, hashGenerator, store);
        app.run(args);
    }

    public App(TextIO io, RandomHashGenerator hashGenerator, Store store) {
        this.io = io;
        this.hashGenerator = hashGenerator;
        this.store = store;
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
        String path = args.get(0);
        String name = store.getName(path);
        String id = hashGenerator.next();
        Checklist checklist = new Checklist(id, name);
        store.save(path, checklist);
    }

}
