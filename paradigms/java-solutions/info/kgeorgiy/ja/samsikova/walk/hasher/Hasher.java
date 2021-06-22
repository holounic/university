package info.kgeorgiy.ja.samsikova.walk.hasher;

import java.io.IOException;
import java.nio.file.Path;

public interface Hasher {
    long hash(Path filePath);
}
