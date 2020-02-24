package expression;

public class CheckedLog extends AbstractBinary {
    private static final String operationSign = "//";
    private static final Priority priority = Priority.HIGH_L;

    public CheckedLog(Operand left, Operand right) {
        super(left, right);
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    public String getOperationSign() {
        return operationSign;
    }

    private int compute(int left, int right) throws ArithmeticException {
        if (right <= 1 || left <= 0) {
            throw new ArithmeticException("нинада так делать");
        }

        int power = 0;
        int res = 1;

        while (res <= left) {
            res *= right;
            power++;
        }
        return power - 1;
    }

    @Override
    protected int operator(int x, int y) {
        return compute(x, y);
    }

    @Override
    public String toString() {
        return left.toString() + " " + operationSign + " " + right.toString();
    }


}
