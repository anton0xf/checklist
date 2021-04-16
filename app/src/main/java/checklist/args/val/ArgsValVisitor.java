package checklist.args.val;

public interface ArgsValVisitor {
    void visitComposite(CompositeArgsVal val);
    void visitArgsBlock(ArgsBlockVal val);
    void visitOption(OptionVal val);
    void visitParametrizedOption(ParametrizedOptionVal val);
    void visitPositionalArg(PositionalArgVal val);
}
