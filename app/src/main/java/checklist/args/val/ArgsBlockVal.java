package checklist.args.val;

import io.vavr.collection.List;

/**
 * Represent sequence of options and positional (may be optional) args
 */
public class ArgsBlockVal implements ArgsVal {
    /**
     * Options list in order
     */
    private final List<OptionVal> opts;

    /**
     * Positional arguments in order
     */
    private final List<PositionalArgVal> args;

    public ArgsBlockVal(List<OptionVal> opts, List<PositionalArgVal> args) {
        this.opts = opts;
        this.args = args;
    }

    public List<OptionVal> getOpts() {
        return opts;
    }

    public List<PositionalArgVal> getArgs() {
        return args;
    }

    @Override
    public void visit(ArgsValVisitor visitor) {
        visitor.visitArgsBlock(this);
    }
}
