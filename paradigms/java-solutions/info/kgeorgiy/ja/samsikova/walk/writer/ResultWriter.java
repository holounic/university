package info.kgeorgiy.ja.samsikova.walk.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ResultWriter {
    private final BufferedWriter out;
    private static final String OUTPUT_FORMAT = "%016x %s\n";

    public ResultWriter(BufferedWriter out) {
        this.out = out;
    }

    public void write(long hash, String fileName) throws IOException {
        out.write(String.format(OUTPUT_FORMAT, hash, fileName));
    }

    public void write(long hash, Path filePath) throws IOException {
        write(hash, filePath.toString());
    }

}
