package checklist.args.val;

/**
 * Represent command line option like "-h" or "--help"
 * , "-d 3", "--depth=3", "--depth 3" and "-opid" (same as "-o pid").
 */
public class OptionVal implements ArgsVal {

    private final String name;

    public OptionVal(String name) {
        this.name = name;
    }

    @Override
    public void visit(ArgsValVisitor visitor) {
        visitor.visitOption(this);
    }
}
