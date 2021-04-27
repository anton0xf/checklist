package checklist.args;

import checklist.args.def.*;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class TextUsageVisitor implements ArgsDefVisitor<Seq<String>> {
    @Override
    public Seq<String> visitArgsWithSubcommand(ArgsWithSubcommandDef def) {
        // TODO implement
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    @Override
    public Seq<String> visitSubcommand(SubcommandDef def) {
        // TODO implement
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    @Override
    public Seq<String> visitArgsBlock(ArgsBlockDef def) {
        // TODO implement
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    @Override
    public Seq<String> visitOption(OptionArgDef def) {
        String shortOpt = def.getShortName()
                .map(name -> OptionsUtil.toShortOpt(name) + ",")
                .getOrElse("");
        String longOpt = OptionsUtil.toLongOpt(def.getName());
        String parameter = def.getParameterName().map(name -> "=" + name).getOrElse("");
        String optUsage = "%3s %s\t%s".formatted(shortOpt, longOpt + parameter, def.getDescription());
        return List.of(optUsage);
    }

    @Override
    public Seq<String> visitPositional(PositionalArgDef def) {
        // TODO implement
        throw new UnsupportedOperationException("Unimplemented yet");
    }
}
