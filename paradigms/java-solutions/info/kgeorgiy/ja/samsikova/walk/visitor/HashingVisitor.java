package info.kgeorgiy.ja.samsikova.walk.visitor;

import info.kgeorgiy.ja.samsikova.walk.hasher.Hasher;
import info.kgeorgiy.ja.samsikova.walk.writer.ResultWriter;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class HashingVisitor extends SimpleFileVisitor<Path> {
    private final Hasher hasher;
    private final ResultWriter writer;

    private static final long FAILED_RESULT = 0;

    public HashingVisitor(Hasher hasher, ResultWriter writer) {
        this.hasher = hasher;
        this.writer = writer;
    }

    private FileVisitResult writeHash(Path filePath, long hash) throws IOException {
        writer.write(hash, filePath);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
        long hash = hasher.hash(filePath);
        return writeHash(filePath, hash);
    }

    @Override
    public FileVisitResult visitFileFailed(Path filePath, IOException e) throws IOException {
        return writeHash(filePath, FAILED_RESULT);
    }
}
