package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.SubcommandVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class SubcommandDef implements ArgsDef<SubcommandVal> {
    private final String name;

    public SubcommandDef(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public <R> R visit(ArgsDefVisitor<R> visitor) {
        return visitor.visitSubcommand(this);
    }

    @Override
    public Tuple2<SubcommandVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        String command = args.headOption()
                .getOrElseThrow(() -> new ArgParseException(
                        "Arguments list is empty (expected subcommand '%s')".formatted(name), args));
        if (!name.equals(command)) {
            throw new ArgParseException("Expected subcommand '%s'".formatted(name), args);
        }
        return Tuple.of(new SubcommandVal(name), args.tail());
    }
}
