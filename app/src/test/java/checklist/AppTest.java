package checklist;

import checklist.io.ConsoleLogger;
import checklist.io.FileSystemUtils;
import checklist.io.Logger;
import io.vavr.CheckedConsumer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    private static final Logger LOG = new ConsoleLogger(true);

    @Test
    void create() throws Throwable {
        withinTempDir((workDir) -> {
            String[] args = {"create", "buy"};
            String out = tapSystemErrNormalized(() -> new App(workDir).run(args));
            assertEquals("Checklist 'buy.checklist' created\n", out);
            assertTrue(new File(workDir, "buy.checklist").exists());
            // TODO check file content
        });
    }

    private void withinTempDir(CheckedConsumer<File> run) throws Throwable {
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
