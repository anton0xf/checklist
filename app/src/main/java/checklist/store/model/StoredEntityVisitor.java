package checklist.store.model;

public interface StoredEntityVisitor<R> {
    R visitString(StoredString str);

    R visitMap(StoredMap map);
}
