package checklist.args.val;

public class SubcommandVal implements ArgsVal {
    private final String name;
    private final ArgsBlockVal args;

    public SubcommandVal(String name, ArgsBlockVal args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public ArgsBlockVal getArgs() {
        return args;
    }
}
