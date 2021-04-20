package checklist.args.def;

import java.util.function.Function;

import checklist.args.ArgParseException;
import checklist.args.OptionsUtil;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.OptionArgVal;
import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;

public class ArgsBlockDef implements ArgsDef<ArgsBlockVal> {
    private final Map<String, OptionArgDef> longOptions;
    private final Map<String, OptionArgDef> shortOptions;
    private final Seq<PositionalArgDef> positional;

    public ArgsBlockDef(Seq<OptionArgDef> options, Seq<PositionalArgDef> positional) {
        this.longOptions = options.toMap(OptionArgDef::getName, Function.identity());
        this.shortOptions = options
                .filter(option -> option.getShortName().isDefined())
                .toMap(optionArgDef -> optionArgDef.getShortName().get(), Function.identity());
        this.positional = positional;
    }

    @Override
    public Tuple2<ArgsBlockVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        ParseState state = new ParseState(args);
        while (state.hasNext()) {
            String arg = state.next();
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

            // TODO handle positional args
            break; // block is over
        }
        // TODO error if not all mandatory positional args present
        return state.getResult();
    }

    private ArgParseException createUnexpectedOptException(String optName, ParseState state) {
        return new ArgParseException("Unexpected option '" + optName + "'",
                state.getArgs());
    }

    private static class ParseState {
        private Seq<String> args;
        private List<OptionArgVal> options = List.empty();
        private List<PositionalArgVal> positional = List.empty();

        public ParseState(Seq<String> args) {
            this.args = args;
        }

        public boolean hasNext() {
            return !args.isEmpty();
        }

        public String next() {
            return args.head();
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
            return Tuple.of(new ArgsBlockVal(options, positional), args);
        }
    }
}
