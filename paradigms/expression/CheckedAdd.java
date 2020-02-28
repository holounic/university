package expression;

import expression.parser.OverflowException;

public class CheckedAdd extends AbstractBinary {
    private static final Priority priority = Priority.LOW_A;
    private static final String operationSign = "+";

    public CheckedAdd(Operand first, Operand second) {
        super(first, second);
    }

    @Override
    protected String getOperationSign() {
        return operationSign;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    private void checkOverflow(int left, int right) {
        if (left > 0 && right > 0) {
            if (Integer.MAX_VALUE - left < right) {
                throw new OverflowException();
            }
            return;
        }
        if (left < 0 && right < 0) {
            if (Integer.MIN_VALUE - left > right || Integer.MIN_VALUE - right > left) {
                throw new OverflowException();
            }
        }
    }

    @Override
    protected int operator(int x, int y) {
        checkOverflow(x, y);
        return x + y;
    }

}
