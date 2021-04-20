package checklist.args;

import org.junit.jupiter.api.Test;

import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsUtilTest {
    @Test
    public void tryParseLong_notOpt() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("arg");
        assertThat(res).isEmpty();
    }

    @Test
    public void tryParseLong_shotOpt() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("-h");
        assertThat(res).isEmpty();
    }

    @Test
    public void tryParseLong_longOpt() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("--help");
        assertThat(res).containsExactly("help");
    }

    @Test
    public void tryParseLong_longParametrizedOpt() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("--sort=time");
        assertThat(res).containsExactly("sort", "time");
    }
}