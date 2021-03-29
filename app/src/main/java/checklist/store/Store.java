package checklist.store;

import checklist.domain.Checklist;

public interface Store {
    String getName(String path);

    void save(String path, Checklist checklist);
}
