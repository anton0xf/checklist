package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class PositionalArgDef implements ArgsDef<PositionalArgVal> {
    private final boolean optional;
    private final String name;

    public static PositionalArgDef optional(String name) {
        return new PositionalArgDef(true, name);
    }

    public PositionalArgDef(String name) {
        this(false, name);
    }

    private PositionalArgDef(boolean optional, String name) {
        this.optional = optional;
        this.name = name;
    }

    public boolean isOptional() {
        return optional;
    }

    public String getName() {
        return name;
    }

    @Override
    public Tuple2<PositionalArgVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        if (args.isEmpty()) {
            throw new ArgParseException("Args is empty", args);
        }
        String arg = args.head();
        if (OptionsUtil.isOpt(arg)) {
            throw new ArgParseException("Expected positional argument, not option", args);
        }
        return Tuple.of(new PositionalArgVal(arg), args.tail());
    }
}
