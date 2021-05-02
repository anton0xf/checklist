package checklist;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import checklist.io.ConsoleLogger;
import checklist.io.FileSystemUtils;
import checklist.io.Logger;
import com.github.stefanbirkner.systemlambda.Statement;
import io.vavr.CheckedConsumer;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static org.assertj.core.api.Assertions.assertThat;

public class TestUtils {
    private static final Logger LOG = new ConsoleLogger(false);

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

    public static String[] toLines(String msg) {
        return msg.lines().toArray(String[]::new);
    }
}
