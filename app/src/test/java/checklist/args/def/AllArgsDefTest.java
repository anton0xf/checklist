package checklist.args.def;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.PositionalArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

class AllArgsDefTest {
    @Test
    public void parse() throws ArgParseException {
        Tuple2<PositionalArgVal, Seq<String>> res = new AllArgsDef<>(new PositionalArgDef("arg"))
                .parse(List.of("val"));
        assertThat(res._1.getValue()).isEqualTo("val");
        assertThat(res._2).isEmpty();
    }

    @Test
    public void parseExtraArgs() {
        AllArgsDef<PositionalArgVal, PositionalArgDef> def = new AllArgsDef<>(new PositionalArgDef("arg"));
        Assertions.assertThatThrownBy(() -> def.parse(List.of("val", "extra")))
                .isInstanceOfSatisfying(ArgParseException.class,
                        ex -> assertThat(ex).hasMessage("Unexpected extra arguments: [extra]"));
    }
}