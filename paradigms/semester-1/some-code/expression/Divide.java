package expression;

public class Divide extends Operation {
    private static final Priority priority = Priority.HIGH_D;
    private static final char operationSign = '/';

    public Divide(Operand left, Operand right) {
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
}
