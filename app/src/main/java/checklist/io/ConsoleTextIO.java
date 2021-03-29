package checklist.io;

public class ConsoleTextIO implements InteractiveTextIO {
    @Override
    public void show(String str) {
        System.out.println(str);
    }

    @Override
    public void showWarn(String str) {
        System.err.println(str);
    }
}
