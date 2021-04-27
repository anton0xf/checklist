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

/**
 * Define command line option like "-h" or "--help",
 * "-d 3", "--depth=3", "--depth 3" and "-opid" (same as "-o pid").
 */
public class OptionArgDef implements ArgsDef<OptionArgVal> {
    public static final String DEFAULT_PARAMETER_NAME = "value";
    private final String name;
    private final Option<String> shortName;
    private final Option<String> parameterName;
    private final String description;

    public OptionArgDef(String name) {
        this(name, Option.none(), Option.none(), "");
    }

    private OptionArgDef(String name, Option<String> shortName, Option<String> parameterName, String description) {
        this.name = name;
        this.shortName = shortName.peek(OptionsUtil::assertShortOptName);
        this.parameterName = parameterName;
        this.description = description;
    }

    public OptionArgDef withShortName(String shortName) {
        return new OptionArgDef(this.name, Option.some(shortName), this.parameterName, this.description);
    }

    public OptionArgDef withParameter(String name) {
        return new OptionArgDef(this.name, this.shortName, Option.some(name), this.description);
    }

    public OptionArgDef withParameter() {
        return withParameter(DEFAULT_PARAMETER_NAME);
    }

    public OptionArgDef withDescription(String description) {
        return new OptionArgDef(this.name, this.shortName, this.parameterName, description);
    }

    public String getName() {
        return name;
    }

    public Option<String> getShortName() {
        return shortName;
    }

    public Option<String> getParameterName() {
        return parameterName;
    }

    public boolean hasParameter() {
        return parameterName.isDefined();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public <R> R visit(ArgsDefVisitor<R> visitor) {
        return visitor.visitOption(this);
    }

    @Override
    public Tuple2<OptionArgVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        if (args.isEmpty()) {
            throw new ArgParseException("Arguments list is empty", args);
        }
        Option<Tuple2<OptionArgVal, Seq<String>>> longOpt = tryParseLong(args);
        if (longOpt.isDefined()) {
            return longOpt.get();
        }
        Option<Tuple2<OptionArgVal, Seq<String>>> shortOpt = tryParseShort(args);
        if (shortOpt.isDefined()) {
            return shortOpt.get();
        }
        throw new ArgParseException("Unexpected argument (expected '%s')".formatted(toLongOpt(name)), args);
    }

    private Option<Tuple2<OptionArgVal, Seq<String>>> tryParseLong(Seq<String> args) throws ArgParseException {
        String arg = args.head();
        Seq<String> parsedLongOpt = tryParseLongOpt(arg);
        if (parsedLongOpt.isEmpty()) {
            return Option.none();
        }
        String optName = parsedLongOpt.head();
        if (!name.equals(optName)) {
            throw new ArgParseException("Unexpected option (expected '%s')".formatted(toLongOpt(name)), args);
        }
        return Option.some(parseLong(parsedLongOpt.tail().headOption(), args));
    }

    private Tuple2<OptionArgVal, Seq<String>> parseLong(Option<String> parameter, Seq<String> args)
            throws ArgParseException {
        Seq<String> otherArgs = args.tail();
        if (hasParameter()) {
            if (parameter.isDefined()) {
                return Tuple.of(new OptionArgVal(name, parameter.get()), otherArgs);
            }
            return parseParametrized(otherArgs);
        } else {
            if (parameter.isDefined()) {
                throw new ArgParseException("Unexpected option parameter", args);
            }
            return Tuple.of(new OptionArgVal(name), otherArgs);
        }
    }

    private Tuple2<OptionArgVal, Seq<String>> parseParametrized(Seq<String> otherArgs) throws ArgParseException {
        String param = otherArgs.headOption()
                .getOrElseThrow(() -> new ArgParseException(
                        "Option '%s' requires parameter but args list is empty".formatted(name), otherArgs));
        if (OptionsUtil.isOpt(param)) {
            throw new ArgParseException("Option '%s' requires parameter".formatted(name), otherArgs);
        }
        return Tuple.of(new OptionArgVal(name, param), otherArgs.tail());
    }

    private Option<Tuple2<OptionArgVal, Seq<String>>> tryParseShort(Seq<String> args) throws ArgParseException {
        if (shortName.isEmpty()) {
            return Option.none();
        }
        String arg = args.head();
        Seq<String> parsedShortOpt = OptionsUtil.tryParseShortOpt(arg);
        if (parsedShortOpt.isEmpty()) {
            return Option.none();
        }
        String optName = parsedShortOpt.head();
        if (!shortName.get().equals(optName)) {
            String msg = "Unexpected option (expected '%s' or '%s')"
                    .formatted(OptionsUtil.toShortOpt(shortName.get()), toLongOpt(name));
            throw new ArgParseException(msg, args);
        }
        return Option.some(parseShort(parsedShortOpt, args.tail()));
    }

    private Tuple2<OptionArgVal, Seq<String>> parseShort(Seq<String> parsedOpt, Seq<String> otherArgs)
            throws ArgParseException {
        Option<String> argRest = parsedOpt.tail().headOption();
        if (hasParameter()) {
            if (argRest.isDefined()) {
                return Tuple.of(new OptionArgVal(name, argRest.get()), otherArgs);
            }
            return parseParametrized(otherArgs);
        }
        Seq<String> argsRest = argRest
                .map(rest -> otherArgs.prepend(OptionsUtil.toShortOpt(rest)))
                .getOrElse(otherArgs);
        return Tuple.of(new OptionArgVal(name), argsRest);
    }
}
