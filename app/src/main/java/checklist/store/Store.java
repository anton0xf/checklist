package checklist.store;

import checklist.domain.Checklist;
import io.vavr.control.Either;

public interface Store {
    /**
     * Extract entity name from path
     */
    String getName(String path);

    /**
     * Save entity and return actual path or error
     */
    Either<String, String> save(String path, Checklist checklist);
}
