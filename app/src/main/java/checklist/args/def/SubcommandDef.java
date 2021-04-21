package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.SubcommandVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class SubcommandDef implements ArgsDef<SubcommandVal> {
    private final String name;
    private final ArgsBlockDef argsBlock;

    public SubcommandDef(String name, ArgsBlockDef argsBlock) {
        this.name = name;
        this.argsBlock = argsBlock;
    }

    @Override
    public Tuple2<SubcommandVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        String command = args.headOption()
                .getOrElseThrow(() -> new ArgParseException("Args is empty", args));
        if (!name.equals(command)) {
            throw new ArgParseException("Expected subcommand '%s'".formatted(name), args);
        }
        Tuple2<ArgsBlockVal, Seq<String>> parsedArgs = argsBlock.parse(args.tail());
        return Tuple.of(new SubcommandVal(name, parsedArgs._1), parsedArgs._2);
    }
}
