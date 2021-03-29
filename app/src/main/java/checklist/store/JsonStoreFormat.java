package checklist.store;

import checklist.json.ObjectMapperFactory;
import checklist.store.model.StoredEntity;
import checklist.store.model.StoredEntityVisitor;
import checklist.store.model.StoredMap;
import checklist.store.model.StoredString;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class JsonStoreFormat implements StoreFormat {
    private final ObjectMapper mapper;

    public JsonStoreFormat(ObjectMapperFactory objectMapperFactory) {
        this.mapper = objectMapperFactory.createMapper();
    }

    @Override
    public void write(StoredEntity entity, Writer out) throws IOException {
        mapper.writer().writeValue(out, entityToJsonNode(entity));
    }

    @Override
    public StoredEntity read(Reader in) throws IOException {
        throw new UnsupportedOperationException("Unimplemented yet");
    }

    // TODO test it
    private JsonNode entityToJsonNode(StoredEntity entity) {
        final JsonNodeFactory nodeFactory = mapper.getNodeFactory();
        return entity.visit(new StoredEntityVisitor<>() {
            @Override
            public JsonNode visitString(StoredString str) {
                return nodeFactory.textNode(str.get());
            }

            @Override
            public JsonNode visitMap(StoredMap map) {
                ObjectNode obj = nodeFactory.objectNode();
                map.iterator().forEach(kv -> obj.set(kv._1, entityToJsonNode(kv._2)));
                return obj;
            }
        });
    }
}
