import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;
import org.testng.Assert;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class ArithmeticsTest {
    final static List<String> assignmentsOnly = List.of("a = 10", "b = 39");
    final static List<String> assignmentMulticharacter = List.of("ababa = 10", "aboba = 39");
    final static List<String> expressionNoVariables = List.of("a = 1 + 9 * 7 + 6 / 3", "b = 14 + 7");
    final static List<String> expressionPrevVariables = List.of("a = 8 + 1", "b = a + a * 3");
    final static List<String> expressionWithParen = List.of("a = 10 * (2 + 3)");
    final static List<String> expressionUpdateYourself = List.of("a = -20", "a = a * 12 * 8 / 4 - 3");
    final static List<String> expressionSimpleFactorial = List.of("a = 3!!");
    final static List<String> expressionFactorial = List.of("a = (1 + 3)! * 6");
    final static List<String> expressionPriorityFactorial = List.of("a = 6 * 2!!");

    @Test
    public void testAssignmentsOnly() throws IOException {
        var result = parseFromString(compose(assignmentsOnly)).values;
        int a = result.getOrDefault("a", -1);
        int b = result.getOrDefault("b", -1);
        Assert.assertEquals(a, 10);
        Assert.assertEquals(b, 39);
    }

    @Test
    public void testAssignmentMulticharacter() throws IOException {
        var result = parseFromString(compose(assignmentMulticharacter)).values;
        int a = result.getOrDefault("ababa", -1);
        int b = result.getOrDefault("aboba", -1);
        Assert.assertEquals(a, 10);
        Assert.assertEquals(b, 39);
    }

    @Test
    public void expressionNoVariables() throws IOException {
        var result = parseFromString(compose(expressionNoVariables)).values;
        int a = result.getOrDefault("a", -1);
        int b = result.getOrDefault("b", -1);
        Assert.assertEquals(a, 1 + 9 * 7 + 6 / 3);
        Assert.assertEquals(b, 14 + 7);
    }

    @Test
    public void expressionPrevVariables() throws IOException {
        var result = parseFromString(compose(expressionPrevVariables)).values;
        int a = result.getOrDefault("a", -1);
        int b = result.getOrDefault("b", -1);
        Assert.assertEquals(a, 8 + 1);
        Assert.assertEquals(b, 9 + 9 * 3);
    }

    @Test
    public void expressionWithParen() throws IOException {
        var result = parseFromString(compose(expressionWithParen)).values;
        int a = result.getOrDefault("a", -1);
        Assert.assertEquals(a, 10 * (2 + 3));
    }

    @Test
    public void expressionUpdateYourself() throws IOException {
        var result = parseFromString(compose(expressionUpdateYourself)).values;
        int a = result.getOrDefault("a", -1);
        Assert.assertEquals(a, (-20) * 12 * 8 / 4 - 3);
    }

    @Test
    public void expressionSimpleFactorial() throws IOException {
        var result = parseFromString(compose(expressionSimpleFactorial)).values;
        int a = result.getOrDefault("a", -1);
        Assert.assertEquals(a, 720);
    }

    @Test
    public void expressionFactorial() throws IOException {
        var result = parseFromString(compose(expressionFactorial)).values;
        int a = result.getOrDefault("a", -1);
        Assert.assertEquals(a, 24 * 6);
    }

    @Test
    public void expressionPriorityFactorial() throws IOException {
        var result = parseFromString(compose(expressionPriorityFactorial)).values;
        int a = result.getOrDefault("a", -1);
        Assert.assertEquals(a, 12);
    }

    private static String compose(List<String> a) {
        return String.join(";\n", a) + ';';
    }

    private static NiceArithmeticParser.ResultContext parseFromString(String inputString) throws IOException {
        return parseFromStream(new ByteArrayInputStream(inputString.getBytes()));
    }

    private static NiceArithmeticParser.ResultContext parseFromStream(InputStream is) throws IOException {
        return getParser(is).result();
    }

    private static NiceArithmeticParser getParser(InputStream is) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(is);
        NiceArithmeticLexer lexer = new NiceArithmeticLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new NiceArithmeticParser(tokens);
    }
}
