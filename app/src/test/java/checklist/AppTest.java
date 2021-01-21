package checklist;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void create() throws Exception {
        String[] args = {"create", "buy"};
        String out = tapSystemErrNormalized(() -> App.main(args));
        assertEquals("Checklist 'buy.checklist' created\n", out);
        // TODO check that file created
        // TODO check file content
    }
}
