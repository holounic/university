package expression;

public class CheckedAdd extends Operation {
    private static final Priority priority = Priority.LOW_A;
    private static final char operationSign = '+';

    public CheckedAdd(Operand first, Operand second) {
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

    private void checkOverflow(int left, int right) {
        if (left > 0 && right > 0) {
            if (Integer.MAX_VALUE - left < right) {
                throw new ArithmeticException("overflow");
            }
            return;
        }

        if (left < 0 && right < 0) {
            if (Integer.MIN_VALUE - left > right) {
                throw new ArithmeticException("overflow");
            }
        }
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x);
            int right = this.right.evaluate(x);
            checkOverflow(left, right);
            return left + right;
        } catch (ArithmeticException e) {
            throw e;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x, y, z);
            int right = this.right.evaluate(x, y, z);
            checkOverflow(left, right);
            return left + right;
        } catch (ArithmeticException e) {
            throw e;
        }

    }
}
