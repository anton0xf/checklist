package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsVal;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

/**
 * Command line arguments definition.
 * Used to parse arguments and to generate usage documentation.
 */
public interface ArgsDef<R extends ArgsVal> {
    <R> R visit(ArgsDefVisitor<R> visitor);
    Tuple2<R, Seq<String>> parse(Seq<String> args) throws ArgParseException;
}
