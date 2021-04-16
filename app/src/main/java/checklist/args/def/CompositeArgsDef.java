package checklist.args.def;

import checklist.args.val.CompositeArgsVal;
import io.vavr.collection.List;

public class CompositeArgsDef implements ArgsDef {
    @Override
    public CompositeArgsVal parse(List<String> args) throws ArgsParseException {
        return null;
    }

    @Override
    public String getUsage() {
        return null;
    }
}
