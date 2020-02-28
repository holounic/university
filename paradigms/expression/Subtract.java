package expression;

public class Subtract extends Operation {
    private static final Priority priority = Priority.LOW_S;
    private static final char operationSign = '-';

    public Subtract(Operand first, Operand second) {
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
}
