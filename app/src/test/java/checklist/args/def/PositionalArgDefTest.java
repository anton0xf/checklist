package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PositionalArgDefTest {
    @Test
    public void parsePosArg() throws ArgParseException {
        Tuple2<PositionalArgVal, Seq<String>> res = new PositionalArgDef("name")
                .parse(List.of("test", "rest"));
        assertThat(res._1.getValue()).isEqualTo("test");
        assertThat(res._2).isEqualTo(List.of("rest"));
    }

    @Test
    public void parsePosArgFromEmptyArgs() {
        assertThatThrownBy(() -> new PositionalArgDef("name").parse(List.empty()))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Args is empty: []"));
    }

    @Test
    public void parsePosArgFromOption() {
        assertThatThrownBy(() -> new PositionalArgDef("name").parse(List.of("--help", "rest")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Expected positional argument, not option: [--help, rest]"));
    }
}