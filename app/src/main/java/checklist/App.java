package checklist;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {
    public static final String CREATE_COMMAND = "create";

    private final File workDir;

    public static void main(String[] args) {
        new App(new File(".")).run(args);
    }

    public App(File workDir) {
        this.workDir = workDir;
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
        // TODO don't add extension if it is already passed
        File file = new File(workDir, name + ".checklist");
        try {
            file.createNewFile();
            // TODO use text IO
            System.err.println(String.format("Checklist '%s' created", file.getName()));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
