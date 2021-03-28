package checklist.io;

import java.io.Writer;

import io.vavr.CheckedConsumer;

public interface IO {
    void write(CheckedConsumer<Writer> fn);

    default void write(String str) {
        write(out -> out.write(str));
    }
}
