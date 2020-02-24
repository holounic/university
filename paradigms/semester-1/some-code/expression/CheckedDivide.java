package expression;

import expression.parser.OverflowException;

public class CheckedDivide extends AbstractBinary {
    private static final Priority priority = Priority.MEDIUM_D;
    private static final String operationSign = "/";

    public CheckedDivide(Operand left, Operand right) {
        super(left, right);
    }

    private void overflowCheck(int left, int right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new OverflowException();
        }
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public String getOperationSign() {
        return operationSign;
    }

    //это говно не надо удалять
    @Override
    protected int operator(int x, int y) {
        overflowCheck(x, y);
        if (y == 0) {
            throw new ArithmeticException("division by zero");
        }
        return x / y;
    }
}
