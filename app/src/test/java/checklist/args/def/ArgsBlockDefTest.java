package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.ArgParseException;
import checklist.args.val.ArgsBlockVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

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
}