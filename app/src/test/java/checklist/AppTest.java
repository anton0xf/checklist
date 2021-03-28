package checklist;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import checklist.io.ConsoleTextIO;
import checklist.io.TextIO;
import checklist.json.ObjectMapperFactory;
import checklist.util.RandomHashGenerator;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    private final ObjectMapper MAPPER = ObjectMapperFactory.createMapper();

    @Test
    void create() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            RandomHashGenerator hashGenerator = new RandomHashGenerator(new Random(0));
            TextIO io = new ConsoleTextIO();
            App app = new App(workDir, hashGenerator, io);
            String out = tapSystemErrNormalized(() -> app.run(new String[]{"create", "buy"}));

            assertEquals("Checklist 'buy.checklist' created\n", out);
            File file = new File(workDir, "buy.checklist");
            assertTrue(file.exists());
            ObjectNode expected = MAPPER.createObjectNode()
                    .put("name", "buy")
                    .put("id", "60b420bb3851d9d4");
            assertJsonEquals(expected, Files.readString(file.toPath()));
        });
    }

    @Test
    void createOther() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            RandomHashGenerator hashGenerator = new RandomHashGenerator(new Random(1));
            TextIO io = new ConsoleTextIO();
            App app = new App(workDir, hashGenerator, io);
            String out = tapSystemErrNormalized(() -> app.run(new String[]{"create", "test.checklist"}));

            assertEquals("Checklist 'test.checklist' created\n", out);
            ObjectNode expected = MAPPER.createObjectNode()
                    .put("name", "test")
                    .put("id", "73d51abbd89cb819");
            assertJsonEquals(expected, Files.readString(new File(workDir, "test.checklist").toPath()));
        });
    }

    private void assertJsonEquals(JsonNode expected, String actualStr) throws JsonProcessingException {
        assertEquals(expected, MAPPER.reader().readTree(actualStr));
    }

    @Test
    public void addFileExtension() {
        assertEquals(".checklist", App.addFileExtension(""));
        assertEquals("asdf.checklist", App.addFileExtension("asdf"));
    }

    @Test
    public void removeFileExtension() {
        assertEquals("", App.removeFileExtension(""));
        assertEquals("a", App.removeFileExtension("a"));
        assertEquals("asdf", App.removeFileExtension("asdf"));
        assertEquals("", App.removeFileExtension(".checklist"));
        assertEquals("a", App.removeFileExtension("a.checklist"));
        assertEquals("asdf", App.removeFileExtension("asdf.checklist"));
    }
}
