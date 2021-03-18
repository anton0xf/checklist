package checklist;

import java.io.File;
import java.io.IOException;

import checklist.io.ConsoleTextIO;
import checklist.io.FileIO;
import checklist.io.TextIO;
import io.vavr.collection.List;

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final String FILE_EXTENSION = ".checklist";

    private final File workDir;
    private final TextIO io;

    public static void main(String[] args) {
        File workDir = new File(".");
        ConsoleTextIO io = new ConsoleTextIO();
        App app = new App(workDir, io);
        app.run(args);
    }

    public App(File workDir, TextIO io) {
        this.workDir = workDir;
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
        File file = new File(workDir, addFileExtension(name));
        try {
            file.createNewFile();
            new FileIO(file).write(String.format("{\"name\":\"%s\"}", name));
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
