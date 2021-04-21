package checklist;

import java.io.File;

import checklist.args.ArgParseException;
import checklist.args.def.AllArgsDef;
import checklist.args.def.ArgsBlockDef;
import checklist.args.def.ArgsDef;
import checklist.args.def.ArgsWithSubcommandDef;
import checklist.args.def.PositionalArgDef;
import checklist.args.def.SubcommandDef;
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

public class App {
    public static final String CREATE_COMMAND = "create";
    public static final int ID_HASH_SIZE = 16;
    private static final ArgsDef<ArgsWithSubcommandVal> ARGS_DEF = createArgsDef();

    private static AllArgsDef<ArgsWithSubcommandVal, ArgsWithSubcommandDef> createArgsDef() {
        List<SubcommandDef> subcommands = List.of(createCreateCommandDef());
        return new AllArgsDef<>(new ArgsWithSubcommandDef(new ArgsBlockDef(), subcommands));
    }

    private static SubcommandDef createCreateCommandDef() {
        List<PositionalArgDef> positional = List.of(new PositionalArgDef("path"));
        return new SubcommandDef(CREATE_COMMAND, new ArgsBlockDef(List.empty(), positional));
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
                case CREATE_COMMAND -> create(subcommand.getArgs().getPositional().map(PositionalArgVal::getValue));
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

    private void create(Seq<String> args) {
        String path = args.head();
        String name = store.getName(path);
        String id = hashGenerator.next();
        Checklist checklist = new Checklist(id, name);
        Either<String, String> result = store.save(path, checklist.toStored());
        result.peek(savedPath -> io.showWarn(String.format("Checklist '%s' created", savedPath)))
                .orElseRun(io::showWarn);
    }

}
