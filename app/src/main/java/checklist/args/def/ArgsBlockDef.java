package checklist.args.def;

import java.util.function.Function;
import java.util.function.Predicate;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.OptionArgVal;
import checklist.args.val.PositionalArgVal;
import checklist.util.Strings;
import io.vavr.Predicates;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

/**
 * Define sequence of options and positional (may be optional) arguments
 */
public class ArgsBlockDef implements ArgsDef<ArgsBlockVal> {
    private static final Predicate<PositionalArgDef> IS_MANDATORY = Predicates.not(PositionalArgDef::isOptional);

    private final Map<String, OptionArgDef> longOptions;
    private final Map<String, OptionArgDef> shortOptions;
    private final Seq<PositionalArgDef> positional;

    private static Seq<PositionalArgDef> validatePositional(Seq<PositionalArgDef> pos) {
        if (pos.dropWhile(IS_MANDATORY).exists(IS_MANDATORY)) {
            throw new IllegalArgumentException("Optional positional parameters should go at the end");
        }
        return pos;
    }

    public ArgsBlockDef() {
        this(List.empty(), List.empty());
    }

    public ArgsBlockDef(Seq<OptionArgDef> options, Seq<PositionalArgDef> positional) {
        this.longOptions = options.toMap(OptionArgDef::getName, Function.identity());
        this.shortOptions = options
                .filter(option -> option.getShortName().isDefined())
                .toMap(optionArgDef -> optionArgDef.getShortName().get(), Function.identity());
        this.positional = validatePositional(positional);
    }

    @Override
    public <R> R visit(ArgsDefVisitor<R> visitor) {
        return visitor.visitArgsBlock(this);
    }

    @Override
    public Tuple2<ArgsBlockVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        ParseState state = new ParseState(args, positional);
        while (state.hasNext()) {
            String arg = state.next();

            if (arg.equals(OptionsUtil.POSITIONAL_SEPARATOR)) {
                state.skip();
                state.parsePositionalArgs();
                break;
            }

            Seq<String> parsedLongOpt = OptionsUtil.tryParseLongOpt(arg);
            if (!parsedLongOpt.isEmpty()) {
                String optName = parsedLongOpt.head();
                OptionArgDef opt = longOptions.get(optName)
                        .getOrElseThrow(() -> createUnexpectedOptException(optName, state));
                state.parseOption(opt);
                continue;
            }

            Seq<String> parsedShortOpt = OptionsUtil.tryParseShortOpt(arg);
            if (!parsedShortOpt.isEmpty()) {
                String optName = parsedShortOpt.head();
                OptionArgDef opt = shortOptions.get(optName)
                        .getOrElseThrow(() -> createUnexpectedOptException(optName, state));
                state.parseOption(opt);
                continue;
            }

            if (state.hasNextPositional()) {
                state.parsePositional();
                continue;
            }

            break; // block is over
        }
        state.checkAllMandatoryPositionalPresent();
        return state.getResult();
    }

    private ArgParseException createUnexpectedOptException(String optName, ParseState state) {
        return new ArgParseException("Unexpected option '" + optName + "'",
                state.args);
    }

    private static class ParseState {
        private Seq<String> args;
        private Seq<PositionalArgDef> restPositionalDefs;
        private List<OptionArgVal> options = List.empty();
        private List<PositionalArgVal> positionalVals = List.empty();

        public ParseState(Seq<String> args, Seq<PositionalArgDef> positionalDefs) {
            this.args = args;
            restPositionalDefs = positionalDefs;
        }

        public boolean hasNext() {
            return args.nonEmpty();
        }

        public String next() {
            return args.head();
        }

        public void skip() {
            args = args.tail();
        }

        public Seq<String> getArgs() {
            return args;
        }

        public void setArgs(Seq<String> args) {
            this.args = args;
        }

        private void parseOption(OptionArgDef opt) throws ArgParseException {
            Tuple2<OptionArgVal, Seq<String>> res = opt.parse(args);
            options = options.append(res._1);
            args = res._2;
        }

        public Tuple2<ArgsBlockVal, Seq<String>> getResult() {
            return Tuple.of(new ArgsBlockVal(options, positionalVals), args);
        }

        public boolean hasNextPositional() {
            return restPositionalDefs.nonEmpty();
        }

        public void parsePositional() throws ArgParseException {
            Tuple2<PositionalArgVal, Seq<String>> res = restPositionalDefs.head().parse(args);
            restPositionalDefs = restPositionalDefs.tail();
            positionalVals = positionalVals.append(res._1);
            args = res._2;
        }

        public void parsePositionalArgs() throws ArgParseException {
            while (hasNextPositional()) {
                parsePositional();
            }
        }

        public void checkAllMandatoryPositionalPresent() throws ArgParseException {
            Seq<PositionalArgDef> restMandatoryArgs = restPositionalDefs.takeWhile(IS_MANDATORY);
            if (restMandatoryArgs.nonEmpty()) {
                String msg = "Expected positional parameters %s".formatted(
                        Strings.seqToString(restMandatoryArgs.map(PositionalArgDef::getName)));
                throw new ArgParseException(msg, args);
            }
        }
    }
}
