package expression;

public class CheckedNegate implements Operand {
    private static final Priority priority = Priority.VAR;
    private final String variable;
    private static final char operationSign = '-';

    public CheckedNegate(String variable) {
        this.variable = variable;
    }

    private void overflowCheck(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        if (this.variable.equals("x")) {
            overflowCheck(x);
            return -x;
        }
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (this.variable) {
            case ("x"):
                overflowCheck(x);
                return -x;
            case ("y"):
                overflowCheck(y);
                return -y;
            case("z"):
                overflowCheck(z);
                return -z;
                default:
                    return 0;
        }
    }

    @Override
    public String toMiniString() {
        return "-" + this.variable;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }
}
