package checklist.io;

public class ConsoleTextIO implements TextIO {
    @Override
    public void print(String str) {
        System.out.println(str);
    }

    @Override
    public void printWarn(String str) {
        System.err.println(str);
    }
}
