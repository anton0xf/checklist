package checklist.json;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperFactory {
    // TODO make not static
    public static ObjectMapper createMapper() {
        return new ObjectMapper();
    }
}
