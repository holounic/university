package expression;

public class CheckedDivide extends Operation {
    private static final Priority priority = Priority.HIGH_D;
    private static final char operationSign = '/';

    public CheckedDivide(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    public char getOperationSign() {
        return operationSign;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    private void overflowCheck(int left, int right) {
        if (left == Integer.MIN_VALUE && right == -1) {
            throw new ArithmeticException("overflow");
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws ArithmeticException {
        int left, right;
        try {
            left = this.left.evaluate(x, y, z);
            right = this.right.evaluate(x, y, z);
            overflowCheck(left, right);
        } catch (ArithmeticException e) {
            throw  e;
        }
        if (right == 0) {
            throw new ArithmeticException("division by zero");
        }
        return left / right;
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        int left, right;
        try {
            left = this.left.evaluate(x);
            right = this.right.evaluate(x);
            overflowCheck(left, right);
        } catch (ArithmeticException e) {
            throw e;
        }
        if (right == 0) {
            throw new ArithmeticException("division by zero");
        }
        return left / right;
    }
}
