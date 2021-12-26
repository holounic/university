import generator.MainGen;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MetaRunner {
    public static final Path FIRST_PATH = Paths.get("fourth/arithmetics.txt");
    public static final Path SECOND_PATH = Paths.get("fourth/python.txt");

    public static void main(String[] args) throws IOException {
        firstTask();
        secondTask();
    }

    private static void firstTask() throws IOException {
        String input = String.join("\n", Files.readAllLines(FIRST_PATH));
        MetaParser.ResultContext resultContext = parseFromString(input);
        var res = resultContext.values;
        new MainGen("arithmetics").generate(res);
    }

    private static void secondTask() throws IOException {
        String input = String.join("\n", Files.readAllLines(SECOND_PATH));
        MetaParser.ResultContext resultContext = parseFromString(input);
        var res = resultContext.values;
        new MainGen("python").generate(res);
    }

    public static MetaParser.ResultContext parseFromString(String inputString) throws IOException {
        return parseFromStream(new ByteArrayInputStream(inputString.getBytes()));
    }

    public static  MetaParser.ResultContext parseFromStream(InputStream is) throws IOException {
        return getParser(is).result();
    }

    private static  MetaParser getParser(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        MetaLexer lexer = new  MetaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new MetaParser(tokens);
    }

}
