package checklist.args.def;

import io.vavr.collection.Seq;

public class ArgParseException extends Throwable {
    private final String msg;
    private final Seq<String> args;

    public ArgParseException(String msg, Seq<String> args) {
        super(msg + ": " + args);
        this.msg = msg;
        this.args = args;
    }

    public ArgParseException(String msg, Seq<String> args, Throwable cause) {
        super(msg + ": " + args, cause);
        this.msg = msg;
        this.args = args;
    }
}
