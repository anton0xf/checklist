package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsWithSubcommandVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArgsWithSubcommandDefTest {
    @Test
    public void parseEmptyArgs() {
        ArgsBlockDef globalArgs = new ArgsBlockDef(List.empty(),
                List.of(new PositionalArgDef("arg")));
        ArgsWithSubcommandDef def = new ArgsWithSubcommandDef(globalArgs,
                List.of(new SubcommandDef("sub", new ArgsBlockDef())));
        assertThatThrownBy(() -> def.parse(List.empty()))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Expected positional parameters [arg]: []"));
    }

    @Test
    public void parseEmptyArgs_noGlobalArgs() {
        ArgsWithSubcommandDef def = new ArgsWithSubcommandDef(new ArgsBlockDef(),
                List.of(new SubcommandDef("sub", new ArgsBlockDef())));
        assertThatThrownBy(() -> def.parse(List.empty()))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Expected subcommand: []"));
    }

    @Test
    public void parseUnexpectedCommand() {
        ArgsWithSubcommandDef def = new ArgsWithSubcommandDef(new ArgsBlockDef(),
                List.of(new SubcommandDef("sub", new ArgsBlockDef())));
        assertThatThrownBy(() -> def.parse(List.of("other")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected subcommand 'other': [other]"));
    }

    @Test
    public void parse() throws ArgParseException {
        ArgsBlockDef globalArgs = new ArgsBlockDef(
                List.of(new OptionArgDef("verbose", "v")),
                List.empty());
        ArgsBlockDef commandArgs = new ArgsBlockDef(
                List.of(new OptionArgDef("debug")),
                List.empty());
        ArgsWithSubcommandDef def = new ArgsWithSubcommandDef(globalArgs,
                List.of(new SubcommandDef("sub", commandArgs)));
        Tuple2<ArgsWithSubcommandVal, Seq<String>> res = def.parse(List.of("-v", "sub", "--debug", "rest"));
        assertThat(res._1.getGlobalArgs())
                .satisfies(args -> assertThat(args.getOptions())
                        .extracting(OptionArgVal::getName)
                        .containsExactly("verbose"));
        assertThat(res._1.getSubcommand())
                .satisfies(command -> {
                    assertThat(command.getName()).isEqualTo("sub");
                    assertThat(command.getArgs().getOptions())
                            .satisfies(args -> assertThat(args)
                                    .extracting(OptionArgVal::getName)
                                    .containsExactly("debug"));
                });
        assertThat(res._2).containsExactly("rest");
    }
}