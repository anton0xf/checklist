package checklist.args;

public class OptionsUtil {
    private static final String OPT_PREFIX = "-";
    private static final String LONG_OPT_PREFIX = "--";
    private static final int SHORT_OPT_LEN = 1;

    public static boolean isOpt(String arg) {
        return arg.startsWith(OPT_PREFIX);
    }

    public static boolean isLongOpt(String arg) {
        return arg.startsWith(LONG_OPT_PREFIX);
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

    public static String getLongOptName(String arg) {
        return arg.substring(LONG_OPT_PREFIX.length());
    }

    public static boolean isLongOptWithName(String arg, String name) {
        return (LONG_OPT_PREFIX + name).equals(arg);
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
