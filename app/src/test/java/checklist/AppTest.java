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
import checklist.io.InteractiveTextIO;
import checklist.json.ObjectMapperFactory;
import checklist.store.ChecklistStore;
import checklist.store.Store;
import checklist.util.RandomHashGenerator;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppTest {
    private static final ObjectMapper MAPPER = new ObjectMapperFactory().createMapper();

    @Test
    void create() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            App app = createApp(workDir, 0);
            String out = tapSystemErrNormalized(() -> app.run(new String[]{"create", "buy"}));

            assertEquals("Checklist 'buy.checklist' created\n", out);
            File file = new File(workDir, "buy.checklist");
            assertTrue(file.exists());
            ObjectNode expected = MAPPER.createObjectNode()
                    .put("name", "buy")
                    .put("id", "60b420bb3851d9d4"); // see RandomHashGeneratorTest
            assertJsonEquals(expected, Files.readString(file.toPath()));
        });
    }

    @Test
    void createOther() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            App app = createApp(workDir, 1);
            String out = tapSystemErrNormalized(() -> app.run(new String[]{"create", "test.checklist"}));

            assertEquals("Checklist 'test.checklist' created\n", out);
            ObjectNode expected = MAPPER.createObjectNode()
                    .put("name", "test")
                    .put("id", "73d51abbd89cb819"); // see RandomHashGeneratorTest
            assertJsonEquals(expected, Files.readString(new File(workDir, "test.checklist").toPath()));
        });
    }

    @Test
    void createAlreadyExists() throws Throwable {
        TestUtils.withTempDir((workDir) -> {
            assertTrue(new File(workDir, "test.checklist").createNewFile());

            App app = createApp(workDir, 0);
            String out = tapSystemErrNormalized(() -> app.run(new String[]{"create", "test.checklist"}));

            assertEquals("File 'test.checklist' already exists\n", out);
        });
    }

    private App createApp(File workDir, int seed) {
        InteractiveTextIO io = new ConsoleTextIO();
        RandomHashGenerator hashGenerator = new RandomHashGenerator(new Random(seed), App.ID_HASH_SIZE);
        ObjectMapperFactory objectMapperFactory = new ObjectMapperFactory();
        Store store = new ChecklistStore(workDir, objectMapperFactory);
        return new App(io, hashGenerator, store);
    }

    private void assertJsonEquals(JsonNode expected, String actualStr) throws JsonProcessingException {
        assertEquals(expected, MAPPER.reader().readTree(actualStr));
    }
}
