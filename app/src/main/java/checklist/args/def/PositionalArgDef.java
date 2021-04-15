package checklist.args.def;

import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class PositionalArgDef implements ArgsDef<PositionalArgVal> {
    @Override
    public Tuple2<PositionalArgVal, Seq<String>> parse(Seq<String> args) {
        // TODO
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
