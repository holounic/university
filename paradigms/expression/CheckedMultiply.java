package expression;
import expression.parser.OverflowException;

public class CheckedMultiply extends AbstractBinary {
    private static final Priority priority = Priority.MEDIUM_M;
    private static final String operationSign = "*";

    public CheckedMultiply(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    protected String getOperationSign() {
        return operationSign;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    private void overflowCheck(int left, int right) {
        if (left > 0 && right > 0 && Integer.MAX_VALUE / left < right) {
            throw new OverflowException();
        }
        if (left > 0 && right < 0 && Integer.MIN_VALUE / left > right) {
            throw new OverflowException();
        }
        if (left < 0 && right < 0 && Integer.MAX_VALUE / left > right) {
            throw new OverflowException();
        }
        if (left < 0 && right > 0 && Integer.MIN_VALUE / right > left) {
            throw new OverflowException();
        }
    }

    @Override
    protected int operator(int x, int y) {
        overflowCheck(x, y);
        return x * y;
    }

}
