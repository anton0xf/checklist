package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

public class OptionArgDef implements ArgsDef<OptionArgVal> {
    private final String name;
    private final Option<String> shortName;

    public OptionArgDef(String name) {
        this(name, Option.none());
    }

    public OptionArgDef(String name, String shortName) {
        this(name, Option.some(shortName));
    }

    public OptionArgDef(String name, Option<String> shortName) {
        this.name = name;
        this.shortName = shortName.peek(OptionsUtil::assertShortOptName);
    }

    public String getName() {
        return name;
    }

    public Option<String> getShortName() {
        return shortName;
    }

    @Override
    public Tuple2<OptionArgVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        String arg = args.headOption()
                .getOrElseThrow(() -> new ArgParseException("Args is empty", args));
        if (isLong(arg)) {
            return Tuple.of(new OptionArgVal(name), args.tail());
        } else if (isShort(arg)) {
            Seq<String> tail = getShortOptRestArgs(args, arg);
            return Tuple.of(new OptionArgVal(name), tail); // TODO handle joined short options
        }
        throw new ArgParseException("Unexpected option (expected '" + name + "')", args);
    }

    private boolean isLong(String arg) {
        return OptionsUtil.isLongOptWithName(arg, name);
    }

    private boolean isShort(String arg) {
        return shortName.map(name -> OptionsUtil.isShortOptWithName(arg, name))
                .getOrElse(false);
    }

    private Seq<String> getShortOptRestArgs(Seq<String> args, String arg) {
        String rest = OptionsUtil.getShortOptRest(arg);
        Seq<String> tail = args.tail();
        return rest.isEmpty() ? tail
                : tail.prepend(OptionsUtil.getShortOpt(rest));
    }
}
