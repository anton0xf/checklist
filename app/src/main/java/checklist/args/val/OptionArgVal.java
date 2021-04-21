package checklist.args.val;

import io.vavr.control.Option;

/**
 * Represent command line option like "-h" or "--help",
 * "-d 3", "--depth=3", "--depth 3" and "-opid" (same as "-o pid").
 */
public class OptionArgVal implements ArgsVal {
    private final String name;
    private final Option<String> value;

    public OptionArgVal(String name) {
        this.name = name;
        this.value = Option.none();
    }

    public OptionArgVal(String name, String value) {
        this.name = name;
        this.value = Option.of(value);
    }

    public String getName() {
        return name;
    }

    public Option<String> getValue() {
        return value;
    }
}
