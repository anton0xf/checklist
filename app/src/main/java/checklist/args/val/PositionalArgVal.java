package checklist.args.val;

public class PositionalArgVal implements ArgsVal {
    private final String value;

    public PositionalArgVal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
