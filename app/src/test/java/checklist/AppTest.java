package checklist;

import checklist.io.ConsoleLogger;
import checklist.io.Logger;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    private static final Logger LOG = new ConsoleLogger(true);

    @Test
    void create() throws Throwable {
        withinTempDir(() -> {
            String[] args = {"create", "buy"};
            String out = tapSystemErrNormalized(() -> App.main(args));
            assertEquals("Checklist 'buy.checklist' created\n", out);
            LOG.debug(() -> Try.of(
                    () -> "cur dir: " + new File(".").getCanonicalPath()
            ).get());
            LOG.debug(() -> "files: " + Arrays.stream(new File(".").list())
                    .collect(Collectors.joining(", ")));
            assertTrue(new File("buy.checklist").exists()); // TODO failed (see below)
            // TODO check file content
        });
    }

    private void withinTempDir(CheckedRunnable run) throws Throwable {
        Path dir = Files.createTempDirectory("checklist-test");
        LOG.debug(() -> String.format("Create temp dir '%s'", dir));
        try {
            // TODO it doesn't work. So some FS interface is needed
            System.setProperty("user.dir", dir.toString());
            run.run();
        } finally {
            Files.walk(dir)
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
            LOG.debug(() -> String.format("Temp dir '%s' deleted", dir));
        }
    }
}
