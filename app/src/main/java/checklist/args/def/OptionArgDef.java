package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import static checklist.args.OptionsUtil.toLongOpt;
import static checklist.args.OptionsUtil.tryParseLongOpt;

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
        Seq<String> parsedLongOpt = tryParseLongOpt(arg);
        if (!parsedLongOpt.isEmpty()) {
            String optName = parsedLongOpt.head();
            if (!name.equals(optName)) {
                throw new ArgParseException("Unexpected option (expected '%s')".formatted(toLongOpt(name)), args);
            }
            return parseLong(parsedLongOpt.tail().headOption(), args.tail());
        }
        Seq<String> parsedShortOpt = OptionsUtil.tryParseShortOpt(arg);
        if (shortName.isDefined() && !parsedShortOpt.isEmpty()) {
            String optName = parsedShortOpt.head();
            if (!shortName.get().equals(optName)) {
                String msg = "Unexpected option (expected '%s' or '%s')"
                        .formatted(OptionsUtil.toShortOpt(shortName.get()), toLongOpt(name));
                throw new ArgParseException(msg, args);
            }
            Seq<String> argsRest = getShortOptRestArgs(parsedShortOpt.tail().headOption(), args.tail());
            return Tuple.of(new OptionArgVal(name), argsRest);
        }
        throw new ArgParseException("Unexpected argument (expected '%s')".formatted(toLongOpt(name)), args);
    }

    private Tuple2<OptionArgVal, Seq<String>> parseLong(Option<String> parameter, Seq<String> otherArgs)
            throws ArgParseException {
        if (hasParameter) {
            if (parameter.isDefined()) {
                return Tuple.of(new OptionArgVal(name, parameter.get()), otherArgs);
            }
            return parseParametrizedLong(otherArgs);
        } else {
            // TODO error if parameter is present
            return Tuple.of(new OptionArgVal(name), otherArgs);
        }
    }

    private Tuple2<OptionArgVal, Seq<String>> parseParametrizedLong(Seq<String> otherArgs) throws ArgParseException {
        String param = otherArgs.headOption()
                .getOrElseThrow(() -> new ArgParseException(
                        "Option '%s' requires parameter but args list is empty".formatted(name), otherArgs));
        if (OptionsUtil.isOpt(param)) {
            throw new ArgParseException("Option '%s' requires parameter".formatted(name), otherArgs);
        }
        return Tuple.of(new OptionArgVal(name, param), otherArgs.tail());
    }

    private Seq<String> getShortOptRestArgs(Option<String> argRest, Seq<String> otherArgs) {
        return argRest
                .map(rest -> otherArgs.prepend(OptionsUtil.toShortOpt(rest)))
                .getOrElse(otherArgs);
    }
}
