package checklist.args;

import checklist.args.def.OptionArgDef;
import checklist.args.def.PositionalArgDef;
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
    public void visitLongOptionWithParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withParameter()
                .withDescription("max depth");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("    --max-depth=value\tmax depth");
    }

    @Test
    public void visitLongOptionWithNamedParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withParameter("num")
                .withDescription("max depth");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("    --max-depth=num\tmax depth");
    }

    @Test
    public void visitShortOption() {
        OptionArgDef def = new OptionArgDef("help")
                .withShortName("h")
                .withDescription("show this help message");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("-h, --help\tshow this help message");
    }

    @Test
    public void visitShortOptionWithParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter()
                .withDescription("max depth");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("-d, --max-depth=value\tmax depth");
    }

    @Test
    public void visitShortOptionWithNamedParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter("num")
                .withDescription("max depth");
        Seq<String> res = new TextUsageVisitor().visitOption(def);
        assertThat(res).containsExactly("-d, --max-depth=num\tmax depth");
    }

    @Test
    public void visitPositionalArg() {
        PositionalArgDef def = new PositionalArgDef("file")
                .withDescription("file name to save");
        Seq<String> res = new TextUsageVisitor().visitPositional(def);
        assertThat(res).containsExactly("file\tfile name to save");
    }
}