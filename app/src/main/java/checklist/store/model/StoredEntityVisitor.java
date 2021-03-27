package checklist.store.model;

public interface StoredEntityVisitor {
    void visitString(StoredString str);
    void visitMap(StoredMap map);
}
