package info.kgeorgiy.ja.samsikova.implementor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Class providing file and directory management utilities.
 */
public class DirectoryUtils {

    /**
     * Deleting file visitor.
     */
    private static final SimpleFileVisitor<Path> cleaner = new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
            if (e == null) {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            } else {
                throw e;
            }
        }
    };

    /**
     * Creates all directories required by the provided file.
     *
     * @param path a {@code Path} locating the desired file
     * @return {@code false} if failed ti create directories, otherwise {@code true}
     */
    public static boolean create(Path path) {
        Path parentPath = path.toAbsolutePath().getParent();
        if (parentPath == null) {
            return false;
        }
        try {
            Files.createDirectories(parentPath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Creates a temporary directory at the specified location.
     *
     * @param path the location for the temporary directory
     * @return {@code Path} instance locating the created temporary directory
     */
    public static Path createTemporal(Path path) {
        if (path == null) {
            return null;
        }
        try {
            return Files.createTempDirectory(path.toAbsolutePath(), "jar-implementor");
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Recursively deletes the specified directory and all its contents.
     *
     * @param path path of the directory to clear
     * @return {@code false} if an error occurs during directory deletion, otherwise {@code true}
     */
    public static boolean clear(Path path) {
        if (path == null) {
            return false;
        }
        try {
            Files.walkFileTree(path.toAbsolutePath(), cleaner);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
