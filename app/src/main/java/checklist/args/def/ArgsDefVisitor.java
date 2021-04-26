package checklist.args.def;

public interface ArgsDefVisitor<R> {
    R visitArgsWithSubcommand(ArgsWithSubcommandDef def);
    R visitSubcommand(SubcommandDef def);
    R visitArgsBlock(ArgsBlockDef def);
    R visitOption(OptionArgDef def);
    R visitPositional(PositionalArgDef def);
}
