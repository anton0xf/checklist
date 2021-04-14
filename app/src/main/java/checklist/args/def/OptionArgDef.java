package checklist.args.def;

import checklist.args.val.ArgsVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

public class OptionArgDef implements ArgsDef {
    private final String name;
    private final Option<String> shortName;

    public OptionArgDef(String name) {
        this(name, Option.none());
    }

    public OptionArgDef(String name, Option<String> shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public Option<String> getShortName() {
        return shortName;
    }

    @Override
    public Tuple2<ArgsVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        String arg = args.headOption()
                .getOrElseThrow(() -> new ArgParseException("Args is empty", args));
        if (isLong(arg)) {
            return Tuple.of(new OptionArgVal(name), args.tail());
        }
        throw new ArgParseException("Unexpected option (expected '" + name + "')", args);
    }

    private boolean isLong(String arg) {
        return ("--" + name).equals(arg);
    }
}
