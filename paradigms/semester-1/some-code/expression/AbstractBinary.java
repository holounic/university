package expression;

public abstract class AbstractBinary extends Operation {

    public AbstractBinary(TripleExpression left, TripleExpression right) {
        super((Operand) left, (Operand)right);
    }

    protected abstract int operator(int x, int y);

    @Override
    public int evaluate(int x, int y, int z) {
        return operator(left.evaluate(x, y, z), right.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) {
        return operator(left.evaluate(x), right.evaluate(x));
    }
}
