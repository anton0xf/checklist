package checklist;

import checklist.io.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    @Test
    void create() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            String[] args = {"create", "buy"};
            TextIO io = new ConsoleTextIO();
            String out = tapSystemErrNormalized(() -> new App(workDir, io).run(args));
            assertEquals("Checklist 'buy.checklist' created\n", out);
            File file = new File(workDir, "buy.checklist");
            assertTrue(file.exists());
            assertEquals("{}", Files.readString(file.toPath()));
        });
    }

    @Test
    public void createFileName() {
        assertEquals("asdf.checklist", App.createFileName("asdf"));
        assertEquals("asdf.checklist", App.createFileName("asdf.checklist"));
    }
}
