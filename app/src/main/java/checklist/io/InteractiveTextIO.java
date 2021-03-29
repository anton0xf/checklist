package checklist.io;

/**
 * Write some text to the user or receive text from him
 */
public interface InteractiveTextIO {
    void show(String str);

    void showWarn(String str);
}
