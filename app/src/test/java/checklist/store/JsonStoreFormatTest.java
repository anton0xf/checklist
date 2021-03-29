package checklist.store;

import checklist.json.ObjectMapperFactory;
import checklist.store.model.StoredEntity;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonStoreFormatTest {
    private static final ObjectMapperFactory MAPPER_FACTORY = new ObjectMapperFactory();
    private static final JsonStoreFormat FORMAT = new JsonStoreFormat(MAPPER_FACTORY);
    private static final JsonNodeFactory NODE_FACTORY = MAPPER_FACTORY.createMapper().getNodeFactory();

    @Test
    void stringToJson() {
        StoredEntity entity = new StoredString("test");
        JsonNode expected = NODE_FACTORY.textNode("test");
        assertEquals(expected, FORMAT.entityToJsonNode(entity));
    }

    @Test
    void simpleMapToJson() {
        StoredEntity entity = new StoredMap().put("a", "1").put("b", "2");
        JsonNode expected = NODE_FACTORY.objectNode()
                .put("a", "1")
                .put("b", "2");
        assertEquals(expected, FORMAT.entityToJsonNode(entity));
    }

    @Test
    void nestedMapToJson() {
        StoredEntity entity = new StoredMap().put("a", "1")
                .put("b", new StoredMap().put("c", "2")
                        .put("d", new StoredMap().put("e", "3")
                                .put("f", new StoredMap().put("g", new StoredMap()))));
        ObjectNode expected = NODE_FACTORY.objectNode();
        expected.put("a", "1")
                .putObject("b")
                .put("c", "2")
                .putObject("d")
                .put("e", "3")
                .putObject("f")
                .putObject("g");
        assertEquals(expected, FORMAT.entityToJsonNode(entity));
    }
}