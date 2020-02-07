package expression;

public class CheckedNegate implements Operand {
    private static final Priority priority = Priority.VAR;
    private final Operand expression;
    private static final char operationSign = '-';

    public CheckedNegate(Operand expression) {
        this.expression = expression;
    }

    private void overflowCheck(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws ArithmeticException {
        int result = ((TripleExpression)this.expression).evaluate(x, y, z);
        overflowCheck(result);
        System.out.println("negate result" + -result);
        return result * -1;
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        int result = ((Expression)this.expression).evaluate(x);
        try {
            overflowCheck(result);
        } catch (ArithmeticException e) {
            throw e;
        }
        return result * -1;
    }

    @Override
    public String toMiniString() {
        return "-" + this.expression.toMiniString();
    }

    @Override
    public String toString() {
        return "(-" + this.expression.toString() + ")";
    }

    @Override
    public Priority getPriority() {
        return priority;
    }
}
