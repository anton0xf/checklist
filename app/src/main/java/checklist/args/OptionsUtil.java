package checklist.args;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.vavr.Predicates;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;

public class OptionsUtil {
    private static final String OPT_PREFIX = "-";
    private static final String LONG_OPT_PREFIX = "--";
    private static final Pattern LONG_OPT_PATTERN = Pattern.compile("^--([^=]+)(?:=(.*))?$");
    private static final int SHORT_OPT_LEN = 1;

    public static boolean isOpt(String arg) {
        return arg.startsWith(OPT_PREFIX);
    }

    public static String toLongOpt(String name) {
        return LONG_OPT_PREFIX + name;
    }

    /**
     * Try to parse long option from command line argument.
     * It returns empty Seq, if it is not long option.
     * It returns singleton Seq of option name, if it contains only name (line "--help").
     * Or it returns option name and value, if it contains both (like "--sort=time").
     */
    public static Seq<String> tryParseLongOpt(String arg) {
        Matcher matcher = LONG_OPT_PATTERN.matcher(arg);
        if (!matcher.matches()) {
            return List.empty();
        }
        return Stream.from(1)
                .take(matcher.groupCount())
                .map(matcher::group)
                .filter(Predicates.isNotNull());
    }

    public static String getShortOptName(String arg) {
        int start = OPT_PREFIX.length();
        return arg.substring(start, start + SHORT_OPT_LEN);
    }

    public static String getShortOpt(String rest) {
        return OPT_PREFIX + rest;
    }

    public static boolean isShortOptWithName(String arg, String name) {
        return arg.startsWith(getShortOpt(name));
    }

    public static void assertShortOptName(String name) {
        if (name.length() != SHORT_OPT_LEN) {
            throw new IllegalArgumentException("Short option len should be %d: '%s'".formatted(SHORT_OPT_LEN, name));
        }
    }

    public static String getShortOptRest(String arg) {
        return arg.substring(OPT_PREFIX.length() + SHORT_OPT_LEN);
    }
}
