package checklist.args;

import checklist.args.def.OptionArgDef;
import io.vavr.collection.Seq;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TextUsageVisitorTest {
    @Test
    public void visitLongOption() {
        OptionArgDef def = new OptionArgDef("help")
                .withDescription("show this help message");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("    --help\tshow this help message");
    }

    @Test
    public void visitShortOption() {
        OptionArgDef def = new OptionArgDef("help")
                .withShortName("h")
                .withDescription("show this help message");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("-h, --help\tshow this help message");
    }
}