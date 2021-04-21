package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsBlockVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArgsBlockDefTest {
    @Test
    public void parseLongOption() throws ArgParseException {
        ArgsBlockDef def = new ArgsBlockDef(
                List.of(new OptionArgDef("help")),
                List.empty());
        Tuple2<ArgsBlockVal, Seq<String>> res = def.parse(List.of("--help", "rest"));
        assertThat(res._1.getOptions()).hasOnlyOneElementSatisfying(
                option -> assertThat(option.getName()).isEqualTo("help"));
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseUnexpectedLongOption() {
        ArgsBlockDef def = new ArgsBlockDef(
                List.of(new OptionArgDef("help")),
                List.empty());
        List<String> args = List.of("--other", "rest");
        assertThatThrownBy(() -> def.parse(args))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected option 'other': [--other, rest]"));
    }

    @Test
    public void parseShortOption() throws ArgParseException {
        ArgsBlockDef def = new ArgsBlockDef(
                List.of(new OptionArgDef("help", "h")),
                List.empty());
        Tuple2<ArgsBlockVal, Seq<String>> res = def.parse(List.of("-h", "rest"));
        assertThat(res._1.getOptions()).hasOnlyOneElementSatisfying(
                option -> assertThat(option.getName()).isEqualTo("help"));
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseShortOptions() throws ArgParseException {
        ArgsBlockDef def = new ArgsBlockDef(
                List.of(new OptionArgDef("verbose", "v"),
                        new OptionArgDef("quiet", "q")),
                List.empty());
        Tuple2<ArgsBlockVal, Seq<String>> res = def.parse(List.of("-vq", "rest"));
        assertThat(res._1.getOptions()).hasSize(2)
                .extracting(OptionArgVal::getName)
                .containsExactly("verbose", "quiet");
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void optionalPositionalShouldGoAtTheEnd() {
        assertThatThrownBy(() -> new ArgsBlockDef(
                List.empty(),
                List.of(PositionalArgDef.optional("o1"), new PositionalArgDef("m1"))))
                .isInstanceOfSatisfying(IllegalArgumentException.class,
                        ex -> assertThat(ex).hasMessage("Optional positional parameters should go at the end"));
    }

    @Test
    public void parsePositional() throws ArgParseException {
        ArgsBlockDef def = new ArgsBlockDef(
                List.empty(),
                List.of(new PositionalArgDef("name")));
        Tuple2<ArgsBlockVal, Seq<String>> res = def.parse(List.of("test", "rest"));
        assertThat(res._1.getOptions()).isEmpty();
        assertThat(res._1.getPositional()).hasOnlyOneElementSatisfying(
                arg -> assertThat(arg.getValue()).isEqualTo("test"));
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseNotEnoughPositional() {
        ArgsBlockDef def = new ArgsBlockDef(
                List.of(new OptionArgDef("help", "h")),
                List.of(new PositionalArgDef("name"),
                        new PositionalArgDef("other")));
        assertThatThrownBy(() -> def.parse(List.of("test", "-h")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Expected positional parameters [other]: []"));
    }
}