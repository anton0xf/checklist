package checklist.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

// TODO separate to interface and implementation
// TODO use wrapper instead of util?
public class FileIO {
    public static void write(String str, File file) {
        try (OutputStreamWriter out = makeOut(file)) {
            out.write(str);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Can not write to '%s'", file.getPath()), ex);
        }
    }

    private static OutputStreamWriter makeOut(File file) {
        try {
            return new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), StandardCharsets.UTF_8);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(String.format("File '%s' not found", file.getPath()), ex);
        }
    }
}
