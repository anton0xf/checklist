package checklist.args.def;

import io.vavr.control.Option;

public class OptionArgDef implements ArgsDef {
    private final String name;
    private final Option<String> shortName;

    public OptionArgDef(String name, Option<String> shortName) {
        this.name = name;
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public Option<String> getShortName() {
        return shortName;
    }
}
