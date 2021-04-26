package checklist.args.def;

import java.util.function.Function;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.ArgsWithSubcommandVal;
import checklist.args.val.SubcommandVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

public class ArgsWithSubcommandDef implements ArgsDef<ArgsWithSubcommandVal> {
    private final ArgsBlockDef globalArgs;
    private final Map<String, SubcommandDef> subcommands;

    public ArgsWithSubcommandDef(ArgsBlockDef globalArgs, Seq<SubcommandDef> subcommands) {
        this.globalArgs = globalArgs;
        this.subcommands = subcommands.toMap(SubcommandDef::getName, Function.identity());
    }

    @Override
    public <R> R visit(ArgsDefVisitor<R> visitor) {
        return visitor.visitArgsWithSubcommand(this);
    }

    @Override
    public Tuple2<ArgsWithSubcommandVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        Tuple2<ArgsBlockVal, Seq<String>> parsedGlobal = globalArgs.parse(args);
        Seq<String> restArgs = parsedGlobal._2;
        String command = restArgs.headOption()
                .getOrElseThrow(() -> new ArgParseException("Expected subcommand", restArgs));
        SubcommandDef subcommand = subcommands.get(command)
                .getOrElseThrow(() -> new ArgParseException("Unexpected subcommand '%s'".formatted(command), args));
        Tuple2<SubcommandVal, Seq<String>> parsedSubcommand = subcommand.parse(restArgs);
        return Tuple.of(new ArgsWithSubcommandVal(parsedGlobal._1, parsedSubcommand._1), parsedSubcommand._2);
    }
}
