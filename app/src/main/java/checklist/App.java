package checklist;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static final String CREATE_COMMAND = "create";

    public static void main(String[] args) {
        if(args.length == 0) {
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

    private static void printHelp() {
        // TODO print help message and usage to stderr
    }

    private static void create(List<String> args) {
        if(args.isEmpty()) {
            // TODO print command usage
            return;
        }
        String name = args.get(0);
        File file = new File(name + ".checklist");
        System.err.println(String.format("Checklist '%s' created", file.getPath()));
    }
}
