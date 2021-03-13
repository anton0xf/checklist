package checklist.io;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileIO implements IO {
    private final File file;

    public FileIO(File file) {
        this.file = file;
    }

    @Override
    public void write(String str) {
        try (OutputStreamWriter out = createWriter()) {
            out.write(str);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("Can not write to '%s'", file.getPath()), ex);
        }
    }

    private OutputStreamWriter createWriter() {
        try {
            return new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), StandardCharsets.UTF_8);
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(String.format("File '%s' not found", file.getPath()), ex);
        }
    }
}
