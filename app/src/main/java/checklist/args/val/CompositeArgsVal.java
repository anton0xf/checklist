package checklist.args.val;

import io.vavr.collection.List;

public class CompositeArgsVal implements ArgsVal {
    private final List<ArgsVal> elements;

    public CompositeArgsVal(List<ArgsVal> elements) {
        this.elements = elements;
    }

    public List<ArgsVal> getElements() {
        return elements;
    }

    @Override
    public void visit(ArgsValVisitor visitor) {
        visitor.visitComposite(this);
    }
}
