package expression;

import expression.parser.OverflowException;

public class CheckedSubtract extends AbstractBinary {
    private static final Priority priority = Priority.LOW_S;
    private static final String operationSign = "-";

    public CheckedSubtract(Operand first, Operand second) {
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

    private void overflowCheck(int left, int right) {
        if (left == 0) {
            if (right == Integer.MIN_VALUE) {
                throw new OverflowException();
            }
            return;
        }
        if (left > 0 && right < 0 && Integer.MAX_VALUE + right < left) {
            throw new OverflowException();
        }
        if (left < 0 && right > 0 && left - Integer.MIN_VALUE < right) {
            throw new OverflowException();
        }
    }


    @Override
    protected int operator(int x, int y) {
        overflowCheck(x, y);
        return x - y;
    }
}
