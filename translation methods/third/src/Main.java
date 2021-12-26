import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        NiceArithmeticParser.ResultContext resultContext = parseFromString("a = 4!;");
        var res = resultContext.values;
        System.out.println(res);
    }

    public static  NiceArithmeticParser.ResultContext parseFromString(String inputString) throws IOException {
        return parseFromStream(new ByteArrayInputStream(inputString.getBytes()));
    }

    public static  NiceArithmeticParser.ResultContext parseFromStream(InputStream is) throws IOException {
        return getParser(is).result();
    }

    private static  NiceArithmeticParser getParser(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        NiceArithmeticLexer lexer = new  NiceArithmeticLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new  NiceArithmeticParser(tokens);
    }
}
