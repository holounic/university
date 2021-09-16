package info.kgeorgiy.ja.samsikova.walk.hasher;

import info.kgeorgiy.ja.samsikova.walk.exception.ExceptionLogger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class PJWHasher implements Hasher {

    private static final int BUFFER_SIZE = 1024;

    private long hashPart(byte[] bytes, long prev, int size) {
        long start = prev;
        for (int i = 0; i < size; ++i) {  
            start = (start << 8) + (bytes[i] & 0xff);
            long high = start & 0xff00_0000_0000_0000L;
            if (high != 0) {
                start ^= high >> 48;
                start &= ~high;
            }
        }
        return start;
    }

    @Override
    public long hash(Path filePath) {
        byte[] bytes = new byte[BUFFER_SIZE];
        try (InputStream in = new BufferedInputStream(Files.newInputStream(filePath))) {
            int uploadedBytes = 0;
            long hash = 0;
            while (uploadedBytes != -1) {
                hash = hashPart(bytes, hash, uploadedBytes);
                uploadedBytes = in.read(bytes);
            }
            return hash;
        } catch (SecurityException e) {
            ExceptionLogger.log(String.format("Access to %s denied%n", filePath.toString()), e);
        } catch (IOException e) {
            ExceptionLogger.log("Error when reading file " + filePath.toString(), e);
        }
        return 0;
    }
}
