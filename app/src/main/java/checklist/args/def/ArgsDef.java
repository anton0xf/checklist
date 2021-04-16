package checklist.args.def;

import checklist.args.val.ArgsVal;
import io.vavr.collection.List;

/**
 * Command line arguments definition.
 * Used to parse arguments and to generate usage documentation.
 */
public interface ArgsDef {
    ArgsVal parse(List<String> args) throws ArgsParseException;
    String getUsage();
}
