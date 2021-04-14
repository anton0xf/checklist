package checklist.args.def;

import org.junit.jupiter.api.Test;

import checklist.args.val.ArgsVal;
import checklist.args.val.OptionArgVal;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

class OptionArgDefTest {
    @Test
    public void parseLong() throws ArgParseException {
        Seq<String> args = List.of("--help", "rest");
        Tuple2<ArgsVal, Seq<String>> res = new OptionArgDef("help").parse(args);
        assertThat(res._1).isInstanceOfSatisfying(OptionArgVal.class,
                option -> assertThat(option.getName()).isEqualTo("help"));
        assertThat(res._2).isEqualTo(List.of("rest"));
    }
}