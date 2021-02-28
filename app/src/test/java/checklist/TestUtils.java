package checklist;

import checklist.io.ConsoleLogger;
import checklist.io.FileSystemUtils;
import checklist.io.Logger;
import io.vavr.CheckedConsumer;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {
    private static final Logger LOG = new ConsoleLogger(true);

    static void withTempDir(CheckedConsumer<File> run) throws Throwable {
        Path dir = Files.createTempDirectory("checklist-test-");
        LOG.debug(() -> String.format("Create temp dir '%s'", dir));
        try {
            run.accept(dir.toFile());
        } finally {
            FileSystemUtils.rmDir(dir);
            LOG.debug(() -> String.format("Temp dir '%s' deleted", dir));
        }
    }
}
