package checklist.args.def;

import checklist.args.val.ArgsVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class PositionalArgDef implements ArgsDef {
    @Override
    public Tuple2<ArgsVal, Seq<String>> parse(Seq<String> args) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
