package checklist.store.model;

public interface StoredEntity {
    void visit(StoredEntityVisitor visitor);

    String getString();
}
