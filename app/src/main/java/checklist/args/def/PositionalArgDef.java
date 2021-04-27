package checklist.args.def;

import checklist.args.ArgParseException;
import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Seq;

public class PositionalArgDef implements ArgsDef<PositionalArgVal> {
    private final boolean optional;
    private final String name;
    private final String description;

    public PositionalArgDef(String name) {
        this(false, name, "");
    }

    private PositionalArgDef(boolean optional, String name, String description) {
        this.optional = optional;
        this.name = name;
        this.description = description;
    }

    public PositionalArgDef optional() {
        return new PositionalArgDef(true, this.name, this.description);
    }

    public PositionalArgDef withDescription(String description) {
        return new PositionalArgDef(this.optional, this.name, description);
    }

    public boolean isOptional() {
        return optional;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public <R> R visit(ArgsDefVisitor<R> visitor) {
        return visitor.visitPositional(this);
    }

    @Override
    public Tuple2<PositionalArgVal, Seq<String>> parse(Seq<String> args) throws ArgParseException {
        if (args.isEmpty()) {
            throw new ArgParseException("Args is empty", args);
        }
        return Tuple.of(new PositionalArgVal(args.head()), args.tail());
    }
}
