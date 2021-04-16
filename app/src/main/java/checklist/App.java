package checklist;

import java.io.File;

import checklist.args.def.ArgsDef;
import checklist.args.def.ArgsParseException;
import checklist.args.val.*;
import checklist.domain.Checklist;
import checklist.io.ConsoleTextIO;
import checklist.io.InteractiveTextIO;
import checklist.json.ObjectMapperFactory;
import checklist.store.ChecklistStore;
import checklist.store.Store;
import checklist.util.RandomHashGenerator;
import io.vavr.collection.List;
import io.vavr.control.Either;

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final int ID_HASH_SIZE = 16;
    private static final ArgsDef ARGS_DEF = createArgsDef();

    private static ArgsDef createArgsDef() {
        return null; // TODO
    }

    private final InteractiveTextIO io;
    private final Store store;
    private final RandomHashGenerator hashGenerator;

    public static void main(String[] args) {
        ConsoleTextIO io = new ConsoleTextIO();
        RandomHashGenerator hashGenerator = new RandomHashGenerator(ID_HASH_SIZE);
        File workDir = new File(".");
        ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();
        Store store = new ChecklistStore(workDir, objectMapperFactory);
        App app = new App(io, hashGenerator, store);
        app.run(args);
    }

    public App(InteractiveTextIO io, RandomHashGenerator hashGenerator, Store store) {
        this.io = io;
        this.hashGenerator = hashGenerator;
        this.store = store;
    }

    void run(String[] args) {
        try {
            ArgsVal parsedArgs = ARGS_DEF.parse(List.of(args));
            parsedArgs.visit(new ArgsHandler());
        } catch (ArgsParseException ex) {
            io.showWarn(ex.getMessage());
            printUsage();
        }
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }
        String command = args[0];
        List<String> commandArgs = List.of(args).tail();
        switch (command) {
            case CREATE_COMMAND -> create(commandArgs);
            default -> {
                printUsage();
                System.exit(1);
            }
        }
    }

    private void printUsage() {
        // TODO move to right place (in parser)
        // io.showWarn("no args");

        io.showWarn(ARGS_DEF.getUsage());
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
        Either<String, String> result = store.save(path, checklist.toStored());
        result.peek(savedPath -> io.showWarn(String.format("Checklist '%s' created", savedPath)))
                .orElseRun(io::showWarn);
    }

    private static class ArgsHandler implements ArgsValVisitor {
        @Override
        public void visitComposite(CompositeArgsVal val) {
            // TODO
        }

        @Override
        public void visitArgsBlock(ArgsBlockVal val) {
            // TODO fill options
        }

        @Override
        public void visitOption(OptionVal val) {
            // TODO
        }

        @Override
        public void visitParametrizedOption(ParametrizedOptionVal val) {
            // TODO
        }

        @Override
        public void visitPositionalArg(PositionalArgVal val) {
            // TODO
        }

        // TODO handle subcommand
    }
}
