package checklist.util;

import io.vavr.collection.Seq;

public class Strings {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static <T> String seqToString(Seq<T> seq) {
        return seq.mkString("[", ", ", "]");
    }
}
