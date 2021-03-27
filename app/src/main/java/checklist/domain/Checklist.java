package checklist.domain;

import checklist.store.model.StoredMap;

public class Checklist {
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";

    private final String id;
    private final String name;

    public Checklist(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StoredMap toStored() {
        return new StoredMap().put(ID_FIELD, id).put(NAME_FIELD, name);
    }

    public static Checklist fromStored(StoredMap stored) {
        return new Checklist(stored.get(ID_FIELD).getString(),
                stored.get(NAME_FIELD).getString());
    }
}
