package checklist.args.def;

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
                        ex -> assertThat(ex).hasMessage("Arguments list is empty: []"));
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
    public void parseParametrizedLongWithoutParameter() {
        OptionArgDef def = new OptionArgDef("sort").withParameter();
        assertThatThrownBy(() -> def.parse(List.of("--sort")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage(
                                "Option 'sort' requires parameter but args list is empty: []"));
    }

    @Test
    public void parseParametrizedLongWithOptionInsteadOfParameter() {
        OptionArgDef def = new OptionArgDef("sort").withParameter();
        assertThatThrownBy(() -> def.parse(List.of("--sort", "-h")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Option 'sort' requires parameter: [-h]"));
    }

    @Test
    public void parseParametrizedLong() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("sort").withParameter();
        Tuple2<OptionArgVal, Seq<String>> res = def
                .parse(List.of("--sort", "name", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("sort");
            assertThat(opt.getValue()).isEqualTo(Option.of("name"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseJoinedParametrizedLong() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("sort").withParameter();
        Tuple2<OptionArgVal, Seq<String>> res = def
                .parse(List.of("--sort=name", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("sort");
            assertThat(opt.getValue()).isEqualTo(Option.of("name"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseLongFlagWithJoinedParameter() throws ArgParseException {
        List<String> args = List.of("--help=value", "rest");
        assertThatThrownBy(() -> new OptionArgDef("help").parse(args))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected option parameter: [--help=value, rest]"));
    }

    @Test
    public void toLongShortOpt() {
        assertThatThrownBy(() -> new OptionArgDef("help").withShortName("hl"))
                .isInstanceOfSatisfying(IllegalArgumentException.class,
                        ex -> assertThat(ex).hasMessage("Short option len should be 1: 'hl'"));
    }

    @Test
    public void parseShort() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("help").withShortName("h");
        Tuple2<OptionArgVal, Seq<String>> res = def.parse(List.of("-h", "rest"));
        assertThat(res._1.getName()).isEqualTo("help");
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseShortFromOtherOption() {
        OptionArgDef def = new OptionArgDef("help").withShortName("h");
        assertThatThrownBy(() -> def.parse(List.of("-o", "rest")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected option (expected '-h' or '--help'): [-o, rest]"));
    }

    @Test
    public void parseJoinedShort() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("help").withShortName("h");
        Tuple2<OptionArgVal, Seq<String>> res = def.parse(List.of("-hr", "rest"));
        assertThat(res._1.getName()).isEqualTo("help");
        assertThat(res._2).isEqualTo(List.of("-r", "rest"));
    }

    @Test
    public void parseParametrizedShort() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter();
        Tuple2<OptionArgVal, Seq<String>> res = def
                .parse(List.of("-d", "2", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("max-depth");
            assertThat(opt.getValue()).isEqualTo(Option.of("2"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseParametrizedShortWithoutParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter();
        assertThatThrownBy(() -> def.parse(List.of("-d")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage(
                                "Option 'max-depth' requires parameter but args list is empty: []"));
    }

    @Test
    public void parseParametrizedShortWithOptionInsteadOfParameter() {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter();
        assertThatThrownBy(() -> def.parse(List.of("-d", "-v")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Option 'max-depth' requires parameter: [-v]"));
    }

    @Test
    public void parseJoinedParametrizedShort() throws ArgParseException {
        OptionArgDef def = new OptionArgDef("max-depth")
                .withShortName("d")
                .withParameter();
        Tuple2<OptionArgVal, Seq<String>> res = def.parse(List.of("-d2", "rest"));
        assertThat(res._1).satisfies(opt -> {
            assertThat(opt.getName()).isEqualTo("max-depth");
            assertThat(opt.getValue()).isEqualTo(Option.of("2"));
        });
        assertThat(res._2).isEqualTo(List.of("rest"));
    }
}