package checklist;

import checklist.args.ArgParseException;
import checklist.args.def.*;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.ArgsWithSubcommandVal;
import checklist.args.val.PositionalArgVal;
import checklist.args.val.SubcommandVal;
import checklist.domain.Checklist;
import checklist.io.ConsoleTextIO;
import checklist.io.InteractiveTextIO;
import checklist.json.ObjectMapperFactory;
import checklist.store.ChecklistStore;
import checklist.store.Store;
import checklist.util.RandomHashGenerator;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import java.io.File;

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final int ID_HASH_SIZE = 16;
    private static final ArgsDef<ArgsWithSubcommandVal> ARGS_DEF = createArgsDef();
    public static final ArgsDef<ArgsBlockVal> CREATE_COMMAND_ARGS = new AllArgsDef<>(new ArgsBlockDef(
            List.empty(),
            List.of(new PositionalArgDef("path"))));

    private static ArgsDef<ArgsWithSubcommandVal> createArgsDef() {
        List<SubcommandDef> subcommands = List.of(new SubcommandDef(CREATE_COMMAND));
        return new ArgsWithSubcommandDef(new ArgsBlockDef(), subcommands);
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
            Tuple2<ArgsWithSubcommandVal, Seq<String>> parsedArgs = ARGS_DEF.parse(List.of(args));
            SubcommandVal subcommand = parsedArgs._1.getSubcommand();
            String command = subcommand.getName();
            switch (command) {
                case CREATE_COMMAND -> create(parsedArgs._2);
                default -> {
                    io.showWarn("Unexpected command '%s'".formatted(command));
                    printHelp();
                    System.exit(1);
                }
            }
        } catch (ArgParseException ex) {
            io.showWarn(ex.getMessage());
            printHelp();
            System.exit(1);
        }
    }

    private void printHelp() {
        // TODO print help message and usage to stderr
        io.showWarn("no args");
    }

    private void create(Seq<String> args) throws ArgParseException {
        Tuple2<ArgsBlockVal, Seq<String>> parsed = CREATE_COMMAND_ARGS.parse(args);
        Seq<PositionalArgVal> positional = parsed._1.getPositional();
        String path = positional.head().getValue();
        String name = store.getName(path);
        String id = hashGenerator.next();
        Checklist checklist = new Checklist(id, name);
        Either<String, String> result = store.save(path, checklist.toStored());
        result.peek(savedPath -> io.showWarn(String.format("Checklist '%s' created", savedPath)))
                .orElseRun(io::showWarn);
    }

}
