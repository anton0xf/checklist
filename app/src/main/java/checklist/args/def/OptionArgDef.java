package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.ArgsVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

public class OptionArgDef implements ArgsDef<OptionArgVal> {
    private final String name;
    private final Option<String> shortName;
    private final boolean hasParameter;

    public OptionArgDef(String name) {
        this(name, Option.none(), false);
    }

    public OptionArgDef(String name, String shortName) {
        this(name, Option.some(shortName), false);
    }

    public static OptionArgDef parametrized(String name) {
        return new OptionArgDef(name, Option.none(), true);
    }

    public static OptionArgDef parametrized(String name, String shortName) {
        return new OptionArgDef(name, Option.some(shortName), true);
    }

    private OptionArgDef(String name, Option<String> shortName, boolean hasParameter) {
        this.name = name;
        this.shortName = shortName.peek(OptionsUtil::assertShortOptName);
        this.hasParameter = hasParameter;
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
            return parseLong(args);
        } else if (isShort(arg)) {
            Seq<String> tail = getShortOptRestArgs(args, arg);
            return Tuple.of(new OptionArgVal(name), tail);
        }
        throw new ArgParseException("Unexpected option (expected '" + name + "')", args);
    }

    private Tuple2<OptionArgVal, Seq<String>> parseLong(Seq<String> args) throws ArgParseException {
        if (hasParameter) {
            return parseParametrizedLong(args);
        } else {
            return Tuple.of(new OptionArgVal(name), args.tail());
        }
    }

    private Tuple2<OptionArgVal, Seq<String>> parseParametrizedLong(Seq<String> args) throws ArgParseException {
        Seq<String> tail = args.tail();
        String param = tail.headOption()
                .getOrElseThrow(() -> new ArgParseException(
                        "Option '%s' requires parameter but args list is empty".formatted(name), tail));
        if (OptionsUtil.isOpt(param)) {
            throw new ArgParseException("Option '%s' requires parameter".formatted(name), tail);
        }
        return Tuple.of(new OptionArgVal(name, param), tail.tail());
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
