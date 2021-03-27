package checklist.store.model;

import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class StoredMap implements StoredEntity {
    private final Map<String, StoredEntity> map;

    public StoredMap() {
        this.map = HashMap.empty();
    }

    public StoredMap(Map<String, StoredEntity> map) {
        this.map = map;
    }

    public StoredMap put(String key, StoredEntity value) {
        return new StoredMap(map.put(key, value));
    }

    public StoredMap put(String key, String value) {
        return this.put(key, new StoredString(value));
    }

    public Iterator<Tuple2<String, StoredEntity>> iterator() {
        return map.iterator();
    }

    public Set<String> keys() {
        return map.keySet();
    }

    public StoredEntity get(String key) {
        return map.get(key)
                .getOrElseThrow(() -> new RuntimeException("Map does not contain key '" + key + "'"));
    }

    @Override
    public void visit(StoredEntityVisitor visitor) {
        visitor.visitMap(this);
    }

    @Override
    public String getString() {
        throw new UnsupportedOperationException("It is map, not string");
    }
}
