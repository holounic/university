package expression;

public class Add extends Operation {
    private static final Priority priority = Priority.LOW;
    private static final char operationSign = '+';

    public Add(Operand first, Operand second) {
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
