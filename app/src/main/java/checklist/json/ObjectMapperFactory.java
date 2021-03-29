package checklist.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
    public ObjectMapper createMapper() {
        return new ObjectMapper();
    }
}
