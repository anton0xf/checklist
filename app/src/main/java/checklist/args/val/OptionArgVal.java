package checklist.args.val;

public class OptionArgVal implements ArgsVal {
    private final String name;

    public OptionArgVal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
