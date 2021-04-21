package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.OptionArgVal;
import checklist.args.val.PositionalArgVal;
import checklist.args.val.SubcommandVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;


class SubcommandDefTest {
    @Test
    public void parseEmptyArgs() {
        assertThatThrownBy(() -> new SubcommandDef("sub", new ArgsBlockDef()).parse(List.empty()))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Arguments list is empty (expected subcommand 'sub'): []"));
    }

    @Test
    public void parseWrongCommand() {
        assertThatThrownBy(() -> new SubcommandDef("sub", new ArgsBlockDef()).parse(List.of("other")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Expected subcommand 'sub': [other]"));
    }

    @Test
    public void parse() throws ArgParseException {
        ArgsBlockDef argsBlock = new ArgsBlockDef(
                List.of(new OptionArgDef("verbose")),
                List.of(new PositionalArgDef("arg")));
        SubcommandDef subcommand = new SubcommandDef("sub", argsBlock);
        Tuple2<SubcommandVal, Seq<String>> res = subcommand.parse(List.of("sub", "--verbose", "arg-val", "rest"));
        assertThat(res._1.getName()).isEqualTo("sub");
        assertThat(res._1.getArgs())
                .satisfies(args -> {
                    assertThat(args.getPositional())
                            .extracting(PositionalArgVal::getValue)
                            .containsExactly("arg-val");
                    assertThat(args.getOptions())
                            .extracting(OptionArgVal::getName)
                            .containsExactly("verbose");
                });
        assertThat(res._2).containsExactly("rest");
    }
}