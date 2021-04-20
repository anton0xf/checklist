package checklist.args;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import checklist.util.Strings;
import io.vavr.Predicates;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;

public class OptionsUtil {
    private static final String OPT_PREFIX = "-";
    private static final String LONG_OPT_PREFIX = "--";
    private static final Pattern LONG_OPT_PATTERN = Pattern.compile("^--([^=]+)(?:=(.*))?$");
    private static final Pattern SHORT_OPT_PATTERN = Pattern.compile("^-(\\w)(.*)$");
    private static final int SHORT_OPT_LEN = 1;

    public static boolean isOpt(String arg) {
        return arg.startsWith(OPT_PREFIX);
    }

    public static String toShortOpt(String rest) {
        return OPT_PREFIX + rest;
    }

    public static String toLongOpt(String name) {
        return LONG_OPT_PREFIX + name;
    }

    /**
     * Try to parse short option from command line argument.
     * It returns empty Seq, if argument is not short option.
     * It returns singleton Seq of option short name, if argument contains only name (e.g. "-h").
     * Or it returns option name and rest, if argument contains something else (e.g. "-vd1").
     */
    public static Seq<String> tryParseShortOpt(String arg) {
        return tryParseOpt(arg, SHORT_OPT_PATTERN);
    }

    /**
     * Try to parse long option from command line argument.
     * It returns empty Seq, if argument is not long option.
     * It returns singleton Seq of option name, if argument contains only name (e.g. "--help").
     * Or it returns option name and value, if argument contains both (e.g. "--sort=time").
     */
    public static Seq<String> tryParseLongOpt(String arg) {
        return tryParseOpt(arg, LONG_OPT_PATTERN);
    }

    private static Seq<String> tryParseOpt(String arg, Pattern pattern) {
        Matcher matcher = pattern.matcher(arg);
        if (!matcher.matches()) {
            return List.empty();
        }
        return Stream.from(1)
                .take(matcher.groupCount())
                .map(matcher::group)
                .filter(Predicates.not(Strings::isNullOrEmpty));
    }

    public static void assertShortOptName(String name) {
        if (name.length() != SHORT_OPT_LEN) {
            throw new IllegalArgumentException("Short option len should be %d: '%s'".formatted(SHORT_OPT_LEN, name));
        }
    }
}
