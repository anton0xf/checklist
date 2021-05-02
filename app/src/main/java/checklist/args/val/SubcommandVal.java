package checklist.args.val;

public class SubcommandVal implements ArgsVal {
    private final String name;

    public SubcommandVal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
