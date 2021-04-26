package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

/**
 * Parse using def, checking that there are no extra arguments
 */
public class AllArgsDef<R extends ArgsVal, T extends ArgsDef<R>> implements ArgsDef<R> {
    private final T def;

    public AllArgsDef(T def) {
        this.def = def;
    }

    @Override
    public <R1> R1 visit(ArgsDefVisitor<R1> visitor) {
        return def.visit(visitor);
    }

    @Override
    public Tuple2<R, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        Tuple2<R, Seq<String>> res = def.parse(args);
        if (res._2.nonEmpty()) {
            throw new ArgParseException("Unexpected extra arguments", res._2);
        }
        return res;
    }

    public R parseAll(Seq<String> args) throws ArgParseException {
        return parse(args)._1;
    }
}
