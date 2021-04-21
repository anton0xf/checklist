package checklist.args;

import org.junit.jupiter.api.Test;

import io.vavr.collection.Seq;

import static org.assertj.core.api.Assertions.assertThat;

class OptionsUtilTest {
    @Test
    public void tryParseShort_notOpt() {
        Seq<String> res = OptionsUtil.tryParseShortOpt("arg");
        assertThat(res).isEmpty();
    }

    @Test
    public void tryParseShort_shortOpt() {
        Seq<String> res = OptionsUtil.tryParseShortOpt("-h");
        assertThat(res).containsExactly("h");
    }

    @Test
    public void tryParseShort_longOpt() {
        Seq<String> res = OptionsUtil.tryParseShortOpt("--help");
        assertThat(res).isEmpty();
    }

    @Test
    public void tryParseShort_joinedShortOpt() {
        Seq<String> res = OptionsUtil.tryParseShortOpt("-vd1");
        assertThat(res).containsExactly("v", "d1");
    }

    @Test
    public void tryParseLong_notOpt() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("arg");
        assertThat(res).isEmpty();
    }

    @Test
    public void tryParseLong_shortOpt() {
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

    @Test
    public void tryParseLong_longOptWithEmptyParam() {
        Seq<String> res = OptionsUtil.tryParseLongOpt("--sort=");
        assertThat(res).containsExactly("sort");
    }
}