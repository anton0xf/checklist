package checklist.args.val;

public class ArgsWithSubcommandVal implements ArgsVal {
    private final ArgsBlockVal globalArgs;
    private final SubcommandVal subcommand;

    public ArgsWithSubcommandVal(ArgsBlockVal globalArgs, SubcommandVal subcommand) {
        this.globalArgs = globalArgs;
        this.subcommand = subcommand;
    }

    public ArgsBlockVal getGlobalArgs() {
        return globalArgs;
    }

    public SubcommandVal getSubcommand() {
        return subcommand;
    }
}
