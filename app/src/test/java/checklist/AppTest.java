package checklist;

import io.vavr.CheckedFunction0;
import io.vavr.CheckedRunnable;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @Test
    void create() throws Throwable {
        withinTempDir(() -> {
            String[] args = {"create", "buy"};
            String out = tapSystemErrNormalized(() -> App.main(args));
            assertEquals("Checklist 'buy.checklist' created\n", out);
            assertTrue(new File("buy.checklist").exists());
            // TODO check file content
        });
    }

    private void withinTempDir(CheckedRunnable run) throws Throwable {
        Path dir = Files.createTempDirectory("checklist-test");
        try {
            System.setProperty("user.dir", dir.toString());
            run.run();
        } finally {
            Files.walk(dir)
                    .map(Path::toFile)
                    .sorted(Comparator.reverseOrder())
                    .forEach(File::delete);
        }
    }
}
