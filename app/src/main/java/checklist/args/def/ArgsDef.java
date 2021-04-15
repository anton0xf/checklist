package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public interface ArgsDef<R extends ArgsVal> {
    Tuple2<R, Seq<String>> parse(Seq<String> args) throws ArgParseException;
}
