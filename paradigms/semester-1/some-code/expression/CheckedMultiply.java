package expression;

public class CheckedMultiply extends Operation {
    private static final Priority priority = Priority.HIGH_M;
    private static final char operationSign = '*';

    public CheckedMultiply(Operand left, Operand right) {
        super(left, right);
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
        int delta = (right * left) / right;
        if (delta != left) {
            throw new ArithmeticException("overflow");
        }
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x);
            int right = this.right.evaluate(x);
            overflowCheck(left, right);
            return left * right;
        } catch (ArithmeticException e) {
            throw e;
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws ArithmeticException {
        try {
            int left = this.left.evaluate(x, y, z);
            int right = this.right.evaluate(x, y, z);
            overflowCheck(left, right);
            return left * right;
        } catch (ArithmeticException e) {
            throw e;
        }

    }
}
