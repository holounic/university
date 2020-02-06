package expression;

public class CheckedNegate implements Operand {
    private static final Priority priority = Priority.VAR;
    private final Variable variable;
    private static final char operationSign = '-';

    public CheckedNegate(String variable) {
        this.variable = new Variable(variable);
    }

    public CheckedNegate(Variable variable) {
        this.variable = variable;
    }

    private void overflowCheck(int x) {
        if (x == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow");
        }
    }

    @Override
    public int evaluate(int x) throws ArithmeticException {
        if (this.variable.evaluate(10) == 10) {
            overflowCheck(x);
            return -x;
        }
        return 0;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (this.variable.evaluate(1, 2, 3)) {
            case (1):
                overflowCheck(x);
                return -x;
            case (2):
                overflowCheck(y);
                return -y;
            case(3):
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
