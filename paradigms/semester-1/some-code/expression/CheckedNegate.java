package expression;
import expression.parser.OverflowException;

public class CheckedNegate extends AbstractUnary {
    private static final Priority priority = Priority.VAR;
    private final Operand expression;
    private static final String operationSign = "-";

    public CheckedNegate(Operand expression) {
        super(expression);
        this.expression = expression;
    }

    private void overflowCheck(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException();
        }
    }

    @Override
    protected int operator(int x) {

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

    @Override
    public int evaluate(int x) {
        return 0;
    }
}
