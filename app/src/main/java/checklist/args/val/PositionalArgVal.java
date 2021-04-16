package checklist.args.val;

public class PositionalArgVal implements ArgsVal {
    @Override
    public void visit(ArgsValVisitor visitor) {
        visitor.visitPositionalArg(this);
    }
}
