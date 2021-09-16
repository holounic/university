package info.kgeorgiy.ja.samsikova.walk;

import info.kgeorgiy.ja.samsikova.walk.exception.ExceptionLogger;
import info.kgeorgiy.ja.samsikova.walk.exception.RecursiveWalkException;
import info.kgeorgiy.ja.samsikova.walk.hasher.Hasher;
import info.kgeorgiy.ja.samsikova.walk.hasher.PJWHasher;
import info.kgeorgiy.ja.samsikova.walk.visitor.HashingVisitor;
import info.kgeorgiy.ja.samsikova.walk.writer.ResultWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecursiveWalk {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final Hasher HASHER = new PJWHasher();

    public static void main(String[] args) {

        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            System.out.println("Bad arguments provided");           //syserr
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        Path inputFilePath;      //final?
        Path outputFilePath;

        try {
            inputFilePath = Paths.get(inputFileName);
        } catch (InvalidPathException e) {
            ExceptionLogger.log("Invalid input file name.", e);
            return;
        }
        if (!Files.isRegularFile(inputFilePath)) {
            System.err.println("Provide file");
            return;
        }

        try {
            outputFilePath = Paths.get(outputFileName);
        } catch (InvalidPathException e) {
            ExceptionLogger.log("Invalid output file name.", e, true);
            return;
        }

        try {
            Path outputParentPath = outputFilePath.getParent();
            if (outputParentPath != null) {
                Files.createDirectories(outputParentPath);
            }
        } catch (InvalidPathException e) {
            ExceptionLogger.log("Invalid output file name", e);
        } catch (IOException e) {
            ExceptionLogger.log("Failed to create output file directory", e);
        }

        try {
            walk(inputFilePath, outputFilePath);
        } catch (RecursiveWalkException e) {
            ExceptionLogger.log("Failed while walking (cannot keep walking)", e);
        }
    }

    private static void walk(Path inputFilePath, Path outputFilePath) throws RecursiveWalkException {
        try (BufferedReader in = Files.newBufferedReader(inputFilePath, CHARSET)) {
            try (BufferedWriter out = Files.newBufferedWriter(outputFilePath, CHARSET)) {
                try {
                    ResultWriter writer = new ResultWriter(out);
                    HashingVisitor visitor = new HashingVisitor(HASHER, writer);
                    String toHashPath;
                    while ((toHashPath = in.readLine()) != null) {
                        try {
                            try {
                                Path rootPath = Paths.get(toHashPath);
                                try {
                                    Files.walkFileTree(rootPath, visitor);
                                } catch (SecurityException e) {
                                    ExceptionLogger.log("Unable to access " + toHashPath, e);
                                }
                            } catch (InvalidPathException e) {
                                ExceptionLogger.log(
                                                 String.format("Unable to convert string %s to path", toHashPath), e);
                                writer.write(0, toHashPath);
                            }
                        } catch (IOException e) {
                            ExceptionLogger.log(
                                    "Failed to write output to file " + outputFilePath.toString(), e);
                        }
                    }
                } catch (IOException e) {
                    ExceptionLogger.log("Error when reading input from file" + inputFilePath.toString(), e);
                }
            } catch (IOException e) {
                ExceptionLogger.log("Error when operating with output file", e);
            } catch (SecurityException e) {
                ExceptionLogger.log("Unable to access output file", e);
            }
        } catch (IOException e) {
            ExceptionLogger.log("Error when opening input file", e);
        } catch (SecurityException e) {
            ExceptionLogger.log("Unable to access input file", e);
        }
    }

}
