package checklist.args.def;

import checklist.args.val.ArgsVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public interface ArgsDef {
    Tuple2<ArgsVal, Seq<String>> parse(Seq<String> args) throws ArgParseException;
}
