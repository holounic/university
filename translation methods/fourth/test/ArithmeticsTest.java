import org.junit.Assert;
import org.junit.Test;

import arithmetics.*;

import java.text.ParseException;

public class ArithmeticsTest {
    private static final String SIMPLE_SUM = "1 - 2 - 3";
    private static final String SIMPLE_MUL = "11 * 9 * 4";
    private static final String MUL_SUM_EXPR = "11 + 6 * 3";
    private static final String PARENS_EXPR = "(11 + 6) * 3";
    private static final String MINUS_EXPR = "2 - 2 - 2 - 2";
    private static final String MINUS_UNARY = "-2";
    private static final String ERROR_NO_OPERAND = "5 +";
    private static final String MISMATCHED_PARENS = "(5 + 3";
    private static final String LEFT_SHIFT = "4 << 2";
    private static final String RIGHT_SHIFT = "4 >> 2";
    private static final String COMPLEX_LEFT_SHIFT = "2 * 4 << (2 + 7)";
    private static final String COMPLEX_RIGHT_SHIFT = "3 + 4 >> 2 >> 5";

    @Test
    public void testSum() throws ParseException {
        test(SIMPLE_SUM, 1 - 2 - 3);
    }

    @Test
    public void testMul() throws ParseException {
        test(SIMPLE_MUL, 11 * 9 * 4);
    }

    @Test
    public void testMulSum() throws ParseException {
        test(MUL_SUM_EXPR, 11 + 6 * 3);
    }

    @Test
    public void testParens() throws ParseException {
        test(PARENS_EXPR, (11 + 6) * 3);
    }

    @Test
    public void testMinusUnary() throws ParseException {
        test(MINUS_UNARY, -2);
    }

    @Test
    public void testMinus() throws ParseException {
        test(MINUS_EXPR, 2 - 2 - 2 - 2);
    }

    @Test(expected = ParseException.class)
    public void testErrorNoOperand() throws ParseException {
        testError(ERROR_NO_OPERAND);
    }

    @Test(expected = ParseException.class)
    public void testErrorMismatchedParens() throws ParseException {
        testError(MISMATCHED_PARENS);
    }

    @Test
    public void testLeftShift() throws ParseException {
        test(LEFT_SHIFT, 4 << 2);
    }

    @Test
    public void testRightShift() throws ParseException {
        test(RIGHT_SHIFT, 4 >> 2);
    }

    @Test
    public void testComplexLeftShift() throws ParseException {
        test(COMPLEX_LEFT_SHIFT, 2 * 4 << (2 + 7));
    }

    @Test
    public void testComplexRightShift() throws ParseException {
        test(COMPLEX_RIGHT_SHIFT, 3 + 4 >> 2 >> 5);
    }

    private void test(String expr, long expected) throws ParseException {
        GeneratedParser parser = new GeneratedParser(expr);
        var result = parser.e();
        Assert.assertNotNull(result);
        Assert.assertEquals((long) result.val, expected);
    }

    private void testError(String expr) throws ParseException {
        new GeneratedParser(expr).e();
    }



}
