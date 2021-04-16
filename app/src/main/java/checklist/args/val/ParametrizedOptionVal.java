package checklist.args.val;

public class ParametrizedOptionVal extends OptionVal {
    private final String param;

    public ParametrizedOptionVal(String name, String param) {
        super(name);
        this.param = param;
    }

    @Override
    public void visit(ArgsValVisitor visitor) {
        visitor.visitParametrizedOption(this);
    }
}
