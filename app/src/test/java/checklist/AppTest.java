package checklist;

import java.io.File;
import java.nio.file.Files;

import checklist.json.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import checklist.io.ConsoleTextIO;
import checklist.io.TextIO;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    private final ObjectMapper MAPPER = ObjectMapperFactory.createMapper();

    @Test
    void create() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            String[] args = {"create", "buy"};
            TextIO io = new ConsoleTextIO();
            String out = tapSystemErrNormalized(() -> new App(workDir, io).run(args));
            assertEquals("Checklist 'buy.checklist' created\n", out);
            File file = new File(workDir, "buy.checklist");
            assertTrue(file.exists());
            // TODO extract Checklist model
            assertJsonEquals(MAPPER.createObjectNode().put("name", "buy"),
                    Files.readString(file.toPath()));
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
