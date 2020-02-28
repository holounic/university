package expression;

public class Multiply extends Operation {
    private static final Priority priority = Priority.HIGH_M;
    private static final char operationSign = '*';

    public Multiply(Operand left, Operand right) {
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
}
