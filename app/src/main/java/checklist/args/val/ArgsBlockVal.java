package checklist.args.val;

import io.vavr.collection.Seq;

public class ArgsBlockVal implements ArgsVal {
    private final Seq<OptionArgVal> options;
    private final Seq<PositionalArgVal> positional;

    public ArgsBlockVal(Seq<OptionArgVal> options, Seq<PositionalArgVal> positional) {
        this.options = options;
        this.positional = positional;
    }

    public Seq<OptionArgVal> getOptions() {
        return options;
    }

    public Seq<PositionalArgVal> getPositional() {
        return positional;
    }
}
