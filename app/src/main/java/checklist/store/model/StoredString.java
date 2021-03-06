package checklist.store.model;

public class StoredString implements StoredEntity {
    private final String value;

    public StoredString(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    @Override
    public <R> R visit(StoredEntityVisitor<R> visitor) {
        return visitor.visitString(this);
    }

    @Override
    public String getString() {
        return get();
    }
}
