package checklist.store.model;

public interface StoredEntity {
    <R> R visit(StoredEntityVisitor<R> visitor);

    String getString();
}
