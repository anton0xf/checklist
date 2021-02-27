package checklist;

import checklist.io.ConsoleTextIO;
import checklist.io.TextIO;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> commandArgs = Arrays.stream(args)
                .skip(1)
                .collect(Collectors.toList());
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
    }

    private void create(List<String> args) {
        if (args.isEmpty()) {
            // TODO print command usage
            return;
        }
        String name = args.get(0);
        File file = new File(workDir, createFileName(name));
        try {
            file.createNewFile();
            io.printWarn(String.format("Checklist '%s' created", file.getName()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static String createFileName(String name) {
        return name.endsWith(FILE_EXTENSION) ? name : name + FILE_EXTENSION;
    }
}
