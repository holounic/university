import org.junit.Test;
import java.text.ParseException;

public class ParserTest {

    final Parser parser = new Parser();
    final String TOTALLY_INCORRECT_INPUT = "Unicorns gained crazy popularity in the late nineties";
    final String NO_ARGS_INPUT = "int func();";
    final String ONE_ARG_INPUT = "void func(int unicorn);";
    final String MANY_ARGS_INPUT = "char char_func(int unicorn, char not_unicorn);";
    final String ASTERISK_TYPE_ARG_INPUT = "char char_func(int *unicorn, char *not_unicorn);";
    final String AMPERSAND_TYPE_ARG_INPUT = "bool dummy_bool_func(int &k);";
    final String NO_SEMICOLON_INPUT = "double func(t arg, t arg)";
    final String ILLEGAL_VAR_INPUT = "123 func();";
    final String NO_FUNC_NAME_INPUT = "bool (    int x, int y    );";
    final String CONST_ARG_INPUT = "int func(const char a);";

    @Test(expected = ParseException.class)
    public void testIncorrectInput() throws ParseException {
        parser.parse(TOTALLY_INCORRECT_INPUT);
    }

    @Test
    public void testConstArg() throws ParseException {
        parser.parse(CONST_ARG_INPUT);
    }

    @Test
    public void testNoArgs() throws ParseException {
        parser.parse(NO_ARGS_INPUT);
    }

    @Test
    public void testOneArg() throws ParseException {
        parser.parse(ONE_ARG_INPUT);
    }

    @Test
    public void testManyArgs() throws ParseException {
        parser.parse(MANY_ARGS_INPUT);
    }

    @Test
    public void testAsteriskTypeArgs() throws ParseException {
        parser.parse(ASTERISK_TYPE_ARG_INPUT);
    }

    @Test
    public void testAmpersandTypeArgs() throws ParseException {
        parser.parse(AMPERSAND_TYPE_ARG_INPUT);
    }

    @Test(expected = ParseException.class)
    public void testNoSemicolon() throws ParseException {
        parser.parse(NO_SEMICOLON_INPUT);
    }

    @Test(expected = ParseException.class)
    public void testIllegalVar() throws ParseException {
        parser.parse(ILLEGAL_VAR_INPUT);
    }

    @Test(expected = ParseException.class)
    public void testNoFuncName() throws ParseException {
        parser.parse(NO_FUNC_NAME_INPUT);
    }

}
