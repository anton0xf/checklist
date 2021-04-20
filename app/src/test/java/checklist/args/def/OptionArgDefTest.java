package checklist.args.def;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionArgDefTest {
    @Test
    public void parseLong() throws ArgParseException {
        Tuple2<OptionArgVal, Seq<String>> res = new OptionArgDef("help")
                .parse(List.of("--help", "rest"));
        assertThat(res._1.getName()).isEqualTo("help");
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseLongFromEmptyArgs() {
        assertThatThrownBy(() -> new OptionArgDef("help").parse(List.empty()))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Args is empty: []"));
    }

    @Test
    public void parseLongFromOtherOption() {
        List<String> args = List.of("--other", "rest");
        assertThatThrownBy(() -> new OptionArgDef("help").parse(args))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected option (expected '--help'): [--other, rest]"));
    }

    @Test
    public void parseLongFromNoOpt() {
        List<String> args = List.of("rest");
        assertThatThrownBy(() -> new OptionArgDef("help").parse(args))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected argument (expected '--help'): [rest]"));
    }

    @Test
    public void toLongShortOpt() {
        assertThatThrownBy(() -> new OptionArgDef("help", "hl"))
                .isInstanceOfSatisfying(IllegalArgumentException.class,
                        ex -> assertThat(ex).hasMessage("Short option len should be 1: 'hl'"));
    }

    @Test
    public void parseShort() throws ArgParseException {
        Tuple2<OptionArgVal, Seq<String>> res = new OptionArgDef("help", "h")
                .parse(List.of("-h", "rest"));
        assertThat(res._1.getName()).isEqualTo("help");
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Disabled
    @Test
    public void parseShortFromOtherOption() {
        List<String> args = List.of("-o", "rest");
        assertThatThrownBy(() -> new OptionArgDef("help", "h").parse(args))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected option (expected '-h' or '--help'): [-r, rest]"));
    }

    @Test
    public void parseJoinedShort() throws ArgParseException {
        Tuple2<OptionArgVal, Seq<String>> res = new OptionArgDef("help", "h")
                .parse(List.of("-hr", "rest"));
        assertThat(res._1.getName()).isEqualTo("help");
        assertThat(res._2).isEqualTo(List.of("-r", "rest"));
    }

    @Test
    public void parseParametrizedLongWithoutParameter() {
        assertThatThrownBy(() -> OptionArgDef.parametrized("sort").parse(List.of("--sort")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage(
                                "Option 'sort' requires parameter but args list is empty: []"));
    }

    @Test
    public void parseParametrizedLongWithOptionInsteadOfParameter() {
        assertThatThrownBy(() -> OptionArgDef.parametrized("sort").parse(List.of("--sort", "-h")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Option 'sort' requires parameter: [-h]"));
    }

    @Test
    public void parseParametrizedLong() throws ArgParseException {
        Tuple2<OptionArgVal, Seq<String>> res = OptionArgDef.parametrized("sort")
                .parse(List.of("--sort", "name", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("sort");
            assertThat(opt.getValue()).isEqualTo(Option.of("name"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseJoinedParametrizedLong() throws ArgParseException {
        Tuple2<OptionArgVal, Seq<String>> res = OptionArgDef.parametrized("sort")
                .parse(List.of("--sort=name", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("sort");
            assertThat(opt.getValue()).isEqualTo(Option.of("name"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }
}