package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.val.ArgsVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionArgDefTest {
    @Test
    public void parseLong() throws ArgParseException {
        Tuple2<ArgsVal, Seq<String>> res = new OptionArgDef("help")
                .parse(List.of("--help", "rest"));
        assertThat(res._1).isInstanceOfSatisfying(OptionArgVal.class,
                option -> assertThat(option.getName()).isEqualTo("help"));
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parseLongFromEmptyArgs() {
        assertThatThrownBy(() -> new OptionArgDef("help").parse(List.empty()))
                .hasMessage("Args is empty: []");
    }

    @Test
    public void parseLongFromOtherOption() {
        List<String> args = List.of("--other", "rest");
        assertThatThrownBy(() -> new OptionArgDef("help").parse(args))
                .hasMessage("Unexpected option (expected 'help'): [--other, rest]");
    }
}