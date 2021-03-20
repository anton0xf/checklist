package checklist.io;

import io.vavr.CheckedConsumer;

import java.io.Writer;

public interface IO {
    void write(CheckedConsumer<Writer> fn);

    default void write(String str) {
        write(out -> out.write(str));
    }
}
