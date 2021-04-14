package checklist.args.def;

import io.vavr.control.Option;

public class ParametrizedOptionArgDef extends OptionArgDef {
    // TODO add 'ParameterValidator validator' argument
    public ParametrizedOptionArgDef(String name, Option<String> shortName) {
        super(name, shortName);
    }
}
