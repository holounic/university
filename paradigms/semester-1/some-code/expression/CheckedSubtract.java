package expression;

public class CheckedSubtract extends Operation {
    private static final Priority priority = Priority.LOW_S;
    private static final char operationSign = '-';

    public CheckedSubtract(Operand first, Operand second) {
        super(first, second);
    }

    @Override
    protected char getOperationSign() {
        return operationSign;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    private void overflowCheck(int left, int right) {
        int delta;
        if (left == 0) {
            if (right == Integer.MIN_VALUE) {
                throw new ArithmeticException("overflow");
            }
            return;
        }
        if (left > 0 && right <= 0) {
            if (left - Integer.MAX_VALUE > right) {
                throw new ArithmeticException("overflow");
            }
            return;
        }
        if (left < 0 && right > 0) {
            if (left - Integer.MIN_VALUE < right) {
                throw new ArithmeticException("overflow");
            }
        }
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x);
            int right = this.right.evaluate(x);
            int result = left - right;
            overflowCheck(left, right);
            return result;
        } catch (ArithmeticException e) {
            throw e;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x, y, z);
            int right = this.right.evaluate(x, y, z);
            int result = left - right;
            overflowCheck(left, right);
            return result;
        } catch (ArithmeticException e) {
            throw e;
        }

    }
}
